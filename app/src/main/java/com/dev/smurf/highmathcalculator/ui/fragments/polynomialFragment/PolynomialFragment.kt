package com.dev.smurf.highmathcalculator.ui.fragments.polynomialFragment

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dev.smurf.highmathcalculator.CalculatorApplication
import com.dev.smurf.highmathcalculator.R
import com.dev.smurf.highmathcalculator.mvp.presenters.PolynomialPresenter
import com.dev.smurf.highmathcalculator.mvp.views.PolynomialViewInterface
import com.dev.smurf.highmathcalculator.ui.CustomRecylerViewLayoutManagers.LayoutManagerWithOffableScroll
import com.dev.smurf.highmathcalculator.ui.POJO.MatrixGroup
import com.dev.smurf.highmathcalculator.ui.Snackbar.DropSnackbar
import com.dev.smurf.highmathcalculator.ui.ViewModels.EditTextViewModel
import com.dev.smurf.highmathcalculator.ui.ViewModels.PolynomialListenerViewModel
import com.dev.smurf.highmathcalculator.ui.adapters.MatrixAdapters.ViewHolders.OnMatrixCalculationGoingViewHolder
import com.dev.smurf.highmathcalculator.ui.adapters.PolynomialAdapters.PolynomialImageAdapter
import com.dev.smurf.highmathcalculator.ui.adapters.PolynomialAdapters.ViewHolder.OnPolynomialCalculationGoingViewHolder
import com.dev.smurf.highmathcalculator.ui.adapters.ViewPagersAdapters.BtnViewPagerFragmentStateAdapter
import com.dev.smurf.highmathcalculator.ui.fragments.ExtraInformationFragments.FullEquationDialogFragment
import com.dev.smurf.highmathcalculator.ui.fragments.InputExceptionsDialogFragments.DefaultInputExceptionDialogFragment
import com.dev.smurf.highmathcalculator.ui.fragments.fragmentInterfaces.Settingable
import com.example.smurf.mtarixcalc.PolynomialGroup
import com.example.smurf.mtarixcalc.PolynomialRecyclerViewModel
import com.example.smurf.mtarixcalc.PolynomialTxtAdapter
import com.example.smurf.mtarixcalc.SwipeToDeleteCallback
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator
import kotlinx.android.synthetic.main.fragment_matrix.*
import kotlinx.android.synthetic.main.fragment_polinom.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import org.jetbrains.anko.toast

@ExperimentalCoroutinesApi
class PolynomialFragment : MvpAppCompatFragment(), PolynomialViewInterface, Settingable,
    PolynomialButtonsGridFirstPageFragment.OnFragmentInteractionListener,
    DefaultInputExceptionDialogFragment.onFragmentInteractionListener,
    FullEquationDialogFragment.onClickListener
{

    //вставляем презентер
    @InjectPresenter
    lateinit var mPolynomialPresenter: PolynomialPresenter

    //some crutch for moxy and setting updates
    init
    {
        mvpDelegate.onCreate()
        mvpDelegate.onAttach()
    }


    //recycler view для полиномов
    private lateinit var mPolynomialRecyclerView: RecyclerView
    private lateinit var mPolynomialRecyclerViewAdapter: PolynomialTxtAdapter
    private lateinit var mPolynomialImageAdapter: PolynomialImageAdapter
    private lateinit var mPolynomialRecyclerViewLayoutManager: LayoutManagerWithOffableScroll

    private lateinit var mBtnMatrixViewPagerAdapter: BtnViewPagerFragmentStateAdapter
    private var benFragmentSet = mutableListOf<Fragment>()

    private val mPolynomialRecyclerViewModel by viewModels<PolynomialRecyclerViewModel>()

    private val mPolynomialEditTextViewModel by viewModels<EditTextViewModel>()

    private val listenerViewModel: PolynomialListenerViewModel<PolynomialFragment> by activityViewModels()

    private var isPaused = false

    private lateinit var errorDialogFragment: DefaultInputExceptionDialogFragment

    private val onClickPolynomialLiveData = MutableLiveData<String>()

    private lateinit var fullEquationDialogFragment: FullEquationDialogFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_polinom, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(mPolynomialPresenter)
        Log.d("lifecycle@", "onCreate")
    }

    override fun onStart()
    {
        super.onStart()

        Log.d("lifecycle@", "onStart")

        val viewTreeObserver = ltPolynomialInput.viewTreeObserver
        if (viewTreeObserver.isAlive)
        {
            viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener
            {
                override fun onGlobalLayout()
                {
                    getMaxSizeOfErrorDialog()
                    ltPolynomialInput.viewTreeObserver.removeOnGlobalLayoutListener(this);
                }
            })
        }

        //инициализация recyclerView для полиномов
        initRecyclerView()

        //init view oager for button fragments
        initViewPager()
        //off view pager swap page by gesture
        //buttonViewPagerPolynomail.isUserInputEnabled = false

        //добавление удаления свайпом
        enableSwipeToDeleteAndUndo()

        btnSwapPolynomial.setOnClickListener {
            val tmp = firstPolinom.text
            firstPolinom.text = secondPolinom.text
            secondPolinom.text = tmp
        }

        btnFirstPolynomialDelete.setOnClickListener {
            firstPolinom.text?.clear()
        }

        btnSecondPolynomialDelete.setOnClickListener {
            secondPolinom.text?.clear()
        }


        polynomialScroller.setOnScrollChangeListener(ExtraScroller(this))
    }

    private class ExtraScroller(val polynomialFragment: PolynomialFragment) :
        NestedScrollView.OnScrollChangeListener
    {
        private val someScrollConst = 300
        override fun onScrollChange(
            v: androidx.core.widget.NestedScrollView?,
            scrollX: Int,
            scrollY: Int,
            oldScrollX: Int,
            oldScrollY: Int
        )
        {
            v ?: return
            //Log.d("recycler@", "bottom ${v.bottom} scrollY $scrollY oldScrollY $oldScrollY height ${-(v.measuredHeight - v.getChildAt(0).measuredHeight)}")
            if ((scrollY == -(v.measuredHeight - v.getChildAt(0).measuredHeight)) || !v.canScrollVertically(
                    1
                ) && v.canScrollVertically(-1)
            )
            {
                polynomialFragment.mPolynomialRecyclerViewLayoutManager.isVerticalScrollEnable =
                    true
                polynomialFragment.polinomRecycler.smoothScrollBy(0, someScrollConst)
                return
            }
            if (!polynomialFragment.mPolynomialRecyclerViewLayoutManager.isVerticalScrollEnable) return
            if (v.canScrollVertically(1) && !v.canScrollVertically(-1))
            {
                polynomialFragment.mPolynomialRecyclerViewLayoutManager.isVerticalScrollEnable =
                    false

            }
        }
    }


    override fun onResume()
    {
        Log.d("lifecycle@", "onResume")
        isPaused = false

        onClickPolynomialLiveData.observe(this, { polynomial ->
            mPolynomialPresenter.onClickPolynomial(polynomial)
        })
        super.onResume()
    }

    override fun onPause()
    {
        Log.d("lifecycle@", "onPause")
        isPaused = true
        mPolynomialEditTextViewModel.firstValue = firstPolinom.text.toString()
        mPolynomialEditTextViewModel.secondValue = secondPolinom.text.toString()

        onClickPolynomialLiveData.removeObservers(this)
        onClickPolynomialLiveData.value = ""
        super.onPause()
    }


    private fun initRecyclerView()
    {
        mPolynomialRecyclerViewLayoutManager = LayoutManagerWithOffableScroll(context)

        mPolynomialRecyclerViewAdapter =
            PolynomialTxtAdapter(this.requireContext(), firstPolinom, secondPolinom)

        val point = Point()
        requireActivity().windowManager.defaultDisplay.getSize(point)

        val margin = 6 * requireContext().resources.displayMetrics.density

        mPolynomialImageAdapter =
            PolynomialImageAdapter(
                this.requireContext(),
                firstPolinom,
                secondPolinom,
                point.x.toFloat() - margin,
                onClickPolynomialLiveData
            )

        mPolynomialRecyclerView = requireView().findViewById(R.id.polinomRecycler)

        mPolynomialRecyclerView.adapter = mPolynomialImageAdapter

        mPolynomialRecyclerView.layoutManager = mPolynomialRecyclerViewLayoutManager

        polinomRecycler.itemAnimator = SlideInRightAnimator()
        polinomRecycler.itemAnimator?.apply {
            addDuration = 400
        }
    }

    private fun enableSwipeToDeleteAndUndo()
    {
        val swipeToDeleteCallback = object : SwipeToDeleteCallback(this.requireContext())
        {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int)
            {

                if (mPolynomialRecyclerView.adapter !is PolynomialImageAdapter)
                {
                    var isUnded = false
                    val position = viewHolder.absoluteAdapterPosition
                    val item = mPolynomialRecyclerViewAdapter.getData(position)

                    mPolynomialRecyclerViewAdapter.removeElement(position)

                    val snackbar = Snackbar
                        .make(polinomFrame, "Item was removed from the list.", Snackbar.LENGTH_LONG)
                    snackbar.setAction("UNDO")
                    {
                        mPolynomialImageAdapter.restoreItem(item, position)
                        isUnded = true
                        mPolynomialRecyclerView.scrollToPosition(position)
                    }

                    snackbar.setActionTextColor(Color.YELLOW)
                    snackbar.show()

                    //mPolynomialRecyclerViewModel.updateList(mPolinomRecyclerViewAdapter.getList().clone() as ArrayList<PolynomialGroup>)
                    if (!isUnded)
                    {
                        mPolynomialPresenter.deleteFromDb(item)
                        mPolynomialRecyclerViewModel.deleteItem(item)
                    }

                }
                else
                {
                    if (viewHolder is OnPolynomialCalculationGoingViewHolder)
                    {
                        val position = viewHolder.absoluteAdapterPosition
                        val item = mPolynomialImageAdapter.getData(position)
                        mPolynomialImageAdapter.removeElement(position)
                        mPolynomialPresenter.calculationCanceled(item.time)

                        val dropSnackbar = DropSnackbar.make(polinomFrame)
                        dropSnackbar.setBackground(CalculatorApplication.context.getDrawable(R.drawable.rectangle_with_outline)!!)
                            .setMessage("Calculation stopped").setProgressBar().setDuration(3000)
                            .show()

                    }
                    else
                    {
                        val position = viewHolder.absoluteAdapterPosition
                        val item = mPolynomialImageAdapter.getData(position)

                        mPolynomialImageAdapter.removeElement(position)


                        val dropSnackbar = DropSnackbar.make(polinomFrame)
                        dropSnackbar.setBackground(CalculatorApplication.context.getDrawable(R.drawable.rectangle_with_outline)!!)
                            .setMessage("Item was removed from the list")
                            .setButton("UNDO", color = Color.BLACK, action = {
                                mPolynomialImageAdapter.restoreItem(item, position)
                                mPolynomialRecyclerView.scrollToPosition(position)
                                mPolynomialPresenter.restoreInDb(item)
                                dropSnackbar.dismiss()
                            }).setProgressBar().setDuration(5000)
                        dropSnackbar.show()

                        mPolynomialPresenter.deleteFromDb(item)
                    }
                }
            }

        }

        val itemTouchhelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchhelper.attachToRecyclerView(mPolynomialRecyclerView)
    }


    override fun addToPolynomialRecyclerView(obj: PolynomialGroup)
    {
        if (mPolynomialRecyclerView.adapter !is PolynomialImageAdapter)
            mPolynomialRecyclerViewAdapter.addElement(obj)
        else mPolynomialImageAdapter.addElement(obj)
        mPolynomialRecyclerViewLayoutManager.scrollToPosition(0)
    }

    override fun showToast(obj: String)
    {
        this.requireContext().toast(obj)
    }

    override fun setRecyclerViewList(ar: MutableList<PolynomialGroup>)
    {
        if (mPolynomialRecyclerView.adapter !is PolynomialImageAdapter)
        {
            mPolynomialRecyclerViewAdapter.setList(ar)
        }
        else
        {
            mPolynomialImageAdapter.setList(ar)
        }
    }


    override fun setImageAdapter()
    {
        if (!isPaused && isRecycleViewInited())
        {
            Log.d("loading@", "image adapter seted")
            mPolynomialImageAdapter.setList(
                mPolynomialRecyclerViewAdapter.getList()
            )
            mPolynomialRecyclerView.adapter = mPolynomialImageAdapter
            mPolynomialImageAdapter.notifyDataSetChanged()
        }
    }

    override fun setTxtAdapter()
    {
        if (!isPaused && isRecycleViewInited())
        {
            mPolynomialRecyclerViewAdapter.setList(
                mPolynomialImageAdapter.getList()
            )
            mPolynomialRecyclerView.adapter = mPolynomialRecyclerViewAdapter
            mPolynomialRecyclerViewAdapter.notifyDataSetChanged()
        }
    }

    override fun updateSettings()
    {
        mPolynomialPresenter.updateSettings()
    }

    override fun restoreFromViewModel()
    {
        //востанавливаемя из view model
        if (!mPolynomialRecyclerViewModel.isEmpty())
        {
            if (mPolynomialRecyclerView.adapter !is PolynomialImageAdapter) mPolynomialRecyclerViewAdapter.setList(
                mPolynomialRecyclerViewModel.getList()
            )
            else mPolynomialImageAdapter.setList(
                mPolynomialRecyclerViewModel.getList()
            )
        }

        firstPolinom.text = SpannableStringBuilder(mPolynomialEditTextViewModel.firstValue)
        secondPolinom.text = SpannableStringBuilder(mPolynomialEditTextViewModel.secondValue)
    }

    override fun saveRecyclerViewToViewModel()
    {
        mPolynomialRecyclerViewModel.updateList(
            (if (mPolynomialRecyclerView.adapter is PolynomialImageAdapter) mPolynomialImageAdapter.getList()
            else mPolynomialRecyclerViewAdapter.getList())
        )
    }

    private fun initViewPager()
    {
        if (isViewPagerInited()) return
        if (activity != null)
        {
            mBtnMatrixViewPagerAdapter =
                BtnViewPagerFragmentStateAdapter(
                    requireActivity(), this
                )
            benFragmentSet.add(PolynomialButtonsGridFirstPageFragment())
            mBtnMatrixViewPagerAdapter.setNewFragmentSet(benFragmentSet)
            buttonViewPagerPolynomail.adapter = mBtnMatrixViewPagerAdapter
        }

    }

    private fun isRecycleViewInited() =
        (::mPolynomialRecyclerView.isInitialized &&
                ::mPolynomialRecyclerViewAdapter.isInitialized &&
                ::mPolynomialImageAdapter.isInitialized &&
                ::mPolynomialRecyclerViewLayoutManager.isInitialized
                )

    private fun isViewPagerInited(): Boolean
    {
        return ::mBtnMatrixViewPagerAdapter.isInitialized
    }

    override fun onBtnDivisionClick()
    {
        mPolynomialPresenter.onDivisionClick(
            firstPolinom.text.toString(),
            secondPolinom.text.toString()
        )
    }

    override fun onBtnMinusClick()
    {
        mPolynomialPresenter.onMinusClick(
            firstPolinom.text.toString(),
            secondPolinom.text.toString()
        )
    }

    override fun onBtnPlusClick()
    {
        mPolynomialPresenter.onPlusClick(
            firstPolinom.text.toString(),
            secondPolinom.text.toString()
        )
    }

    override fun onBtnTimesClick()
    {
        mPolynomialPresenter.onTimesClick(
            firstPolinom.text.toString(),
            secondPolinom.text.toString()
        )
    }

    override fun onBtnRootsOfAClick()
    {
        mPolynomialPresenter.onRootsOfClick(firstPolinom.text.toString())
    }

    override fun onBtnRootsOfBClick()
    {
        mPolynomialPresenter.onRootsOfClick(secondPolinom.text.toString())
    }

    override fun onBtnSwitchFPClick()
    {
        mPolynomialPresenter.onSwitchBtnFragmentClick(1)
    }

    override fun setBtnFragment(position: Int)
    {
        buttonViewPagerPolynomail.setCurrentItem(position, true)
    }

    override fun setObserver()
    {
        listenerViewModel.updateListener(this)
    }

    override fun getMaxSizeOfErrorDialog()
    {
        val point = Point()
        requireActivity().windowManager.defaultDisplay.getSize(point)

        mPolynomialPresenter.setMaxDialogSize(
            ltPolynomialInput.measuredWidth.toFloat(),
            ltPolynomialInput.measuredHeight.toFloat(),
            point.x.toFloat(),
            point.y.toFloat()
        )
    }

    override fun btnOkPressed()
    {
        mPolynomialPresenter.onErrorDialogBtnOkPressed()
    }

    override fun showErrorDialog(
        errorBitmap: Bitmap,
        width: Float,
        height: Float,
        errorText: String
    )
    {
        dismissErrorDialog()
        Log.d("errorDialog@", "show dialog")
        errorDialogFragment =
            DefaultInputExceptionDialogFragment(errorBitmap, this, width, height, errorText)
        errorDialogFragment.show(childFragmentManager, "ERROR_DIALOG")
    }

    override fun dismissErrorDialog()
    {
        Log.d("errorDialog@", "dismiss")
        if (::errorDialogFragment.isInitialized && errorDialogFragment.isVisible) errorDialogFragment.dismiss()
    }

    override fun startLoadingInRecyclerView()
    {
        if (mPolynomialRecyclerView.adapter is PolynomialImageAdapter)
        {
            Log.d("loading@", "start loading")
            mPolynomialImageAdapter.startLoading()
        }
        else Log.d("loading@", "txt adapter setted")
    }

    override fun stopLoadingInRecyclerView()
    {
        if (mPolynomialRecyclerView.adapter is PolynomialImageAdapter)
        {
            mPolynomialImageAdapter.stopLoading()
        }
    }

    override fun clearRecyclerView()
    {
        if (polinomRecycler.adapter is PolynomialImageAdapter) mPolynomialImageAdapter.clear()
        else mPolynomialRecyclerViewAdapter.clear()
    }

    override fun setTopPosition()
    {
        polynomialScroller.scrollTo(0, 0)
    }

    override fun displayError(message: String)
    {
        DropSnackbar.make(polinomFrame).setMessage(message, color = Color.BLACK)
            .setProgressBar()
            .setBackground(CalculatorApplication.context.getDrawable(R.drawable.rectangle_error)!!)
            .setDuration(3000).show()
    }

    override fun startCalculation(polynomialGroup: PolynomialGroup)
    {
        if (polinomRecycler.adapter is PolynomialImageAdapter)
        {
            mPolynomialImageAdapter.addElement(polynomialGroup)
            mPolynomialRecyclerViewLayoutManager.scrollToPosition(0)
        }
    }

    override fun stopAllCalculations()
    {
        if (polinomRecycler.adapter is PolynomialImageAdapter)
        {
            mPolynomialImageAdapter.removeAllCalculation()
        }
    }

    override fun calculationFailed(polynomialGroup: PolynomialGroup)
    {
        if (polinomRecycler.adapter is PolynomialImageAdapter)
        {
            mPolynomialImageAdapter.removeCalculation(polynomialGroup)
        }
    }

    override fun calculationCompleted(polynomialGroup: PolynomialGroup)
    {
        if (polinomRecycler.adapter is PolynomialImageAdapter)
        {
            mPolynomialImageAdapter.stopCalculation(polynomialGroup)
        }
    }


    override fun btnMatrixDialogOkClicked()
    {
        mPolynomialPresenter.onEquationDialogBtnOkClick()
    }

    override fun showPolynomialDialog(
        polynomial: String,
        width: Float,
        height: Float,
        matrixBitmap: Bitmap
    )
    {
        fullEquationDialogFragment = FullEquationDialogFragment(
            polynomial,
            matrixBitmap,
            width,
            height,
            this,
            firstPolinom,
            secondPolinom
        )
        fullEquationDialogFragment.show(
            childFragmentManager,
            "MATRIX_DIALOG"
        )
    }

    override fun dismissPolynomialDialog()
    {
        if (::fullEquationDialogFragment.isInitialized && fullEquationDialogFragment.isVisible) fullEquationDialogFragment.dismiss()
    }
}




