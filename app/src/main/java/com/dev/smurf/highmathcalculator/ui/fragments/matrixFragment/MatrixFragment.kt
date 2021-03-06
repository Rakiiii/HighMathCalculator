package com.dev.smurf.highmathcalculator.ui.fragments.matrixFragment

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
import androidx.recyclerview.widget.RecyclerView
import com.dev.smurf.highmathcalculator.CalculatorApplication
import com.dev.smurf.highmathcalculator.R
import com.dev.smurf.highmathcalculator.mvp.presenters.MatrixPresenter
import com.dev.smurf.highmathcalculator.mvp.views.MatrixViewInterface
import com.dev.smurf.highmathcalculator.ui.CustomRecylerViewLayoutManagers.LayoutManagerWithOffableScroll
import com.dev.smurf.highmathcalculator.ui.POJO.MatrixGroup
import com.dev.smurf.highmathcalculator.ui.Snackbar.DropSnackbar
import com.dev.smurf.highmathcalculator.ui.ViewModels.EditTextViewModel
import com.dev.smurf.highmathcalculator.ui.ViewModels.MatrixListenerViewModel
import com.dev.smurf.highmathcalculator.ui.adapters.ViewPagersAdapters.BtnViewPagerFragmentStateAdapter
import com.dev.smurf.highmathcalculator.ui.adapters.MatrixAdapters.MatrixAdapter
import com.dev.smurf.highmathcalculator.ui.adapters.MatrixAdapters.MatrixAdapterImageView
import com.dev.smurf.highmathcalculator.ui.adapters.MatrixAdapters.ViewHolders.OnMatrixCalculationGoingViewHolder
import com.dev.smurf.highmathcalculator.ui.fragments.ExtraInformationFragments.FullEquationDialogFragment
import com.dev.smurf.highmathcalculator.ui.fragments.InputExceptionsDialogFragments.DefaultInputExceptionDialogFragment
import com.dev.smurf.highmathcalculator.ui.fragments.fragmentInterfaces.Settingable
import com.example.smurf.mtarixcalc.MatrixRecyclerViewModel
import com.example.smurf.mtarixcalc.SwipeToDeleteCallback
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator
import kotlinx.android.synthetic.main.fragment_matrix.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import org.jetbrains.anko.toast


@ExperimentalCoroutinesApi
class MatrixFragment : MvpAppCompatFragment(), MatrixViewInterface, Settingable,
    MatrixButtonGridFragmentFirstPage.onFragmentInteractionListener,
    MatrixButtonGridFragmentSecondPage.onFragmentInteractionListener,
    DefaultInputExceptionDialogFragment.onFragmentInteractionListener,
    FullEquationDialogFragment.onClickListener
{

    @InjectPresenter
    lateinit var mMatrixPresenter: MatrixPresenter


    private lateinit var mMatrixRecyclerView: RecyclerView
    private lateinit var mMatrixRecyclerLayoutManager: LayoutManagerWithOffableScroll
    private lateinit var mMatrixRecyclerTextAdapter: MatrixAdapter
    private lateinit var mMatrixRecyclerImageAdapter: MatrixAdapterImageView
    private lateinit var mBtnMatrixViewPagerAdapter: BtnViewPagerFragmentStateAdapter

    private lateinit var errorDialogFragment: DefaultInputExceptionDialogFragment
    private lateinit var fullEquationDialogFragment: FullEquationDialogFragment

    private val mMatrixRecyclerViewModel by viewModels<MatrixRecyclerViewModel>()

    private val mMatrixEdittextViewModel by viewModels<EditTextViewModel>()

    private var isPaused = false

    private var btnFragmentSet = mutableListOf<Fragment>()

    private val matrixListenerViewModel: MatrixListenerViewModel<MatrixFragment> by activityViewModels()

    private val onClickMatrixLiveData = MutableLiveData<Pair<String,String>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        Log.d("lifecycle@", "onCreateView")

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_matrix, container, false)

    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(mMatrixPresenter)


        Log.d("lifecycle@", "onCreate")
    }

    override fun onStart()
    {
        super.onStart()

        val viewTreeObserver = ltMatrixInput.viewTreeObserver
        if (viewTreeObserver.isAlive)
        {
            viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener
            {
                override fun onGlobalLayout()
                {
                    getMaxSizeOfErrorDialog()
                    ltMatrixInput.viewTreeObserver.removeOnGlobalLayoutListener(this);
                }
            })
        }

        matrixListenerViewModel.updateListener(this)

        Log.d("lifecycle@", "onStart")

        //инициализация recycler view
        initRecyclerView()

        //initiali veapager with bottons
        initViewPager()

        //удаление и возврат по свайпу из recycler view
        enableSwipeToDeleteAndUndo()

        //off view pager swap page by gesture
        //buttonViewPager.isUserInputEnabled = false

        //swap matrix btn listener
        btnSwap.setOnClickListener {
            val tmp = firstMatrix.text
            firstMatrix.text = secondMatrix.text
            secondMatrix.text = tmp
        }

        btnFirstMatrixDelete.setOnClickListener {
            firstMatrix.text?.clear()
        }
        btnSecondMatrixDelete.setOnClickListener {
            secondMatrix.text?.clear()
        }

        //matrixRecycler.addOnScrollListener(ToScroller.createToScroller(this)!!)
        scroll.setOnScrollChangeListener(ExtraScroller(this))

    }

    private class ExtraScroller(val matrixFragment: MatrixFragment) :
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
            if ((scrollY == -(v.measuredHeight - v.getChildAt(0).measuredHeight)) || !v.canScrollVertically(
                    1
                ) && v.canScrollVertically(-1)
            )
            {
                matrixFragment.mMatrixRecyclerLayoutManager.isVerticalScrollEnable = true
                matrixFragment.matrixRecycler.smoothScrollBy(0, someScrollConst)
                return
            }
            if (!matrixFragment.mMatrixRecyclerLayoutManager.isVerticalScrollEnable) return
            if (v.canScrollVertically(1) && !v.canScrollVertically(-1))
            {
                matrixFragment.mMatrixRecyclerLayoutManager.isVerticalScrollEnable =
                    false

            }

        }
    }


    override fun onResume()
    {
        isPaused = false
        Log.d("lifecycle@", "onResume")
        onClickMatrixLiveData.observe(this,
            { matrixString -> mMatrixPresenter.matrixInViewHolderClicked(matrixString) })
        super.onResume()
    }


    override fun onPause()
    {
        isPaused = true
        Log.d("lifecycle@", "onPause")
        mMatrixEdittextViewModel.firstValue = firstMatrix.text.toString()
        mMatrixEdittextViewModel.secondValue = secondMatrix.text.toString()
        onClickMatrixLiveData.removeObservers(this)
        onClickMatrixLiveData.value = Pair("","")
        super.onPause()
    }


    //утсановка нового списка элементов для recycler view
    override fun setRecyclerViewList(ar: MutableList<MatrixGroup>)
    {
        if (matrixRecycler.adapter !is MatrixAdapterImageView)
        {
            mMatrixRecyclerTextAdapter.setList(ar)
        }
        else
        {
            mMatrixRecyclerImageAdapter.setList(ar)
        }
    }


    //инициализация recyclerView
    private fun initRecyclerView()
    {
        if (isRecycleViewInitted()) return
        mMatrixRecyclerLayoutManager =
            LayoutManagerWithOffableScroll(
                context
            )//LinearLayoutManager(context)

        mMatrixRecyclerTextAdapter =
            MatrixAdapter(
                requireContext(),
                firstMatrix,
                secondMatrix
            )
        val point = Point()
        requireActivity().windowManager.defaultDisplay.getSize(point)
        val margin = 47 * requireContext().resources.displayMetrics.density


        mMatrixRecyclerImageAdapter =
            MatrixAdapterImageView(
                requireContext(),
                firstMatrix,
                secondMatrix,
                point.x.toFloat() - margin,
                onClickMatrixLiveData
            )

        mMatrixRecyclerView = requireView().findViewById(R.id.matrixRecycler)

        mMatrixRecyclerView.layoutManager = mMatrixRecyclerLayoutManager

        mMatrixRecyclerView.adapter = mMatrixRecyclerImageAdapter

        matrixRecycler.itemAnimator = SlideInRightAnimator()
        matrixRecycler.itemAnimator?.apply {
            addDuration = 400
        }
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
            btnFragmentSet.add(MatrixButtonGridFragmentFirstPage())
            btnFragmentSet.add(MatrixButtonGridFragmentSecondPage())
            mBtnMatrixViewPagerAdapter.setNewFragmentSet(btnFragmentSet)
            buttonViewPager.adapter = mBtnMatrixViewPagerAdapter

        }

    }

    private fun isViewPagerInited(): Boolean
    {
        return ::mBtnMatrixViewPagerAdapter.isInitialized
    }

    override fun addToRecyclerView(obj: MatrixGroup)
    {
        if (matrixRecycler.adapter !is MatrixAdapterImageView)
            mMatrixRecyclerTextAdapter.addNewElem(obj)
        else
            mMatrixRecyclerImageAdapter.addNewElem(obj)
        mMatrixRecyclerLayoutManager.scrollToPosition(0)
    }

    override fun showToast(obj: String)
    {
        requireContext().toast(obj)
    }

    override fun setImageAdapter()
    {
        if (!isPaused && isRecycleViewInitted())
        {
            Log.d("loading@", "image adapter seted")
            mMatrixRecyclerImageAdapter.setList(
                mMatrixRecyclerTextAdapter.getList()
            )
            mMatrixRecyclerView.adapter = mMatrixRecyclerImageAdapter
            mMatrixRecyclerImageAdapter.notifyDataSetChanged()
        }
    }

    //set text adapter
    override fun setTextAdapter()
    {
        if (!isPaused && isRecycleViewInitted())
        {
            mMatrixRecyclerTextAdapter.setList(
                mMatrixRecyclerImageAdapter.getList()
            )
            mMatrixRecyclerView.adapter = mMatrixRecyclerTextAdapter
            mMatrixRecyclerTextAdapter.notifyDataSetChanged()
        }
    }


    //add swipe deletting behaviour
    @ExperimentalCoroutinesApi
    private fun enableSwipeToDeleteAndUndo()
    {
        val swipeToDeleteCallback = object : SwipeToDeleteCallback(requireContext())
        {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int)
            {
                if (matrixRecycler.adapter is MatrixAdapterImageView)
                {
                    if (viewHolder is OnMatrixCalculationGoingViewHolder)
                    {
                        val position = viewHolder.absoluteAdapterPosition
                        val item = mMatrixRecyclerImageAdapter.getData(position)
                        mMatrixRecyclerImageAdapter.removeElement(position)
                        mMatrixPresenter.calculationCanceled(item.time)

                        val dropSnackbar = DropSnackbar.make(matrixFrame)
                        dropSnackbar.setBackground(CalculatorApplication.context.getDrawable(R.drawable.rectangle_with_outline)!!)
                            .setMessage("Calculation stopped").setProgressBar().setDuration(3000)
                            .show()

                    }
                    else
                    {
                        val position = viewHolder.absoluteAdapterPosition
                        val item = mMatrixRecyclerImageAdapter.getData(position)

                        mMatrixRecyclerImageAdapter.removeElement(position)


                        val dropSnackbar = DropSnackbar.make(matrixFrame)
                        dropSnackbar.setBackground(CalculatorApplication.context.getDrawable(R.drawable.rectangle_with_outline)!!)
                            .setMessage("Item was removed from the list")
                            .setButton("UNDO", color = Color.BLACK, action = {
                                mMatrixRecyclerImageAdapter.restoreItem(position, item)
                                mMatrixRecyclerView.scrollToPosition(position)
                                mMatrixPresenter.restoreInDb(item)
                                dropSnackbar.dismiss()
                            }).setProgressBar().setDuration(5000)
                        dropSnackbar.show()
                        mMatrixPresenter.deleteFromDb(item)
                    }
                }
                else
                {
                    var isUnded = false

                    val position = viewHolder.absoluteAdapterPosition
                    val item = mMatrixRecyclerTextAdapter.getData(position)

                    mMatrixRecyclerTextAdapter.removeElement(position)


                    val snackbar = Snackbar
                        .make(matrixFrame, "Item was removed from the list.", Snackbar.LENGTH_LONG)
                    snackbar.setAction("UNDO")
                    {
                        mMatrixRecyclerTextAdapter.restoreItem(position, item)
                        isUnded = true
                        mMatrixRecyclerView.scrollToPosition(position)
                    }

                    snackbar.setActionTextColor(Color.YELLOW)
                    snackbar.show()

                    //mMatrixRecyclerViewModel.updateList(matrixRecyclerAdapter.getList())
                    if (!isUnded)
                    {
                        mMatrixPresenter.deleteFromDb(item)
                        mMatrixRecyclerViewModel.deleteItem(item)
                    }

                }
            }
        }

        val itemTouchhelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchhelper.attachToRecyclerView(mMatrixRecyclerView)
    }


    //updating settings
    override fun updateSettings()
    {
        mMatrixPresenter.updateSettings()
    }

    //get data from view model
    override fun restoreFromViewModel()
    {
        //востонавливаемся из view model
        if (!mMatrixRecyclerViewModel.isEmpty())
        {
            Log.d("lifecycle@", "restored")
            if (mMatrixRecyclerView.adapter is MatrixAdapterImageView)
            {
                mMatrixRecyclerImageAdapter.setList(
                    mMatrixRecyclerViewModel.getList()
                )
                Log.d("lifecycle@", "restored in image view")
            }
            else mMatrixRecyclerTextAdapter.setList(
                mMatrixRecyclerViewModel.getList()
            )
        }

        Log.d("edit@", "restore called:" + (firstMatrix == null).toString())

        firstMatrix.text = SpannableStringBuilder(mMatrixEdittextViewModel.firstValue)
        secondMatrix.text = SpannableStringBuilder(mMatrixEdittextViewModel.secondValue)

    }

    //checks is recycler view inited
    private fun isRecycleViewInitted() =
        (::mMatrixRecyclerView.isInitialized &&
                ::mMatrixRecyclerLayoutManager.isInitialized &&
                ::mMatrixRecyclerImageAdapter.isInitialized &&
                ::mMatrixRecyclerTextAdapter.isInitialized
                )


    override fun btnDeterminantClicked()
    {
        mMatrixPresenter.onDeterminantClick(firstMatrix.text.toString())
    }

    override fun btnInverseClicked()
    {
        Log.d("edit@", "inv call:" + (firstMatrix == null).toString())
        mMatrixPresenter.onInversClick(firstMatrix.text.toString())
    }

    override fun btnPlusClicked()
    {
        mMatrixPresenter.onPlusClick(
            firstMatrix.text.toString(),
            secondMatrix.text.toString()
        )
    }

    override fun btnMinusClicked()
    {
        mMatrixPresenter.onMinusClick(
            firstMatrix.text.toString(),
            secondMatrix.text.toString()
        )
    }

    override fun btnFirstTimeSecondClicked()
    {
        mMatrixPresenter.onTimesClick(firstMatrix.text.toString(), secondMatrix.text.toString())
    }

    override fun btnSecondTimesFirstClicked()
    {
        mMatrixPresenter.onTimesClick(secondMatrix.text.toString(), firstMatrix.text.toString())
    }

    override fun btnSwitchFPClicked()
    {
        mMatrixPresenter.btnSwitchClicked(1)
    }

    override fun setBtnFragment(position: Int)
    {
        buttonViewPager.setCurrentItem(position, true)
    }

    override fun btnEighnvalueClicked()
    {
        mMatrixPresenter.btnEighnvalueClicked(firstMatrix.text.toString())
    }

    override fun btnEighnvectorClicked()
    {
        mMatrixPresenter.btnEighnvectorClicked(firstMatrix.text.toString())
    }

    override fun btnNegativeClicked()
    {
        mMatrixPresenter.btnNegativeClicked(firstMatrix.text.toString())
    }

    override fun btnPositiveClicked()
    {
        mMatrixPresenter.btnPositiveClicked(firstMatrix.text.toString())
    }

    override fun btnRankClicked()
    {
        mMatrixPresenter.btnRankClicked(firstMatrix.text.toString())
    }

    override fun btnSolveSystemClicked()
    {
        mMatrixPresenter.btnSolveSystemClicked(firstMatrix.text.toString())
    }

    override fun btnSwitchSPClicked()
    {
        mMatrixPresenter.btnSwitchClicked(0)
    }


    override fun saveListRecyclerViewViewModel()
    {
        mMatrixRecyclerViewModel.updateList(
            (if (matrixRecycler.adapter is MatrixAdapterImageView) mMatrixRecyclerImageAdapter.getList()
            else mMatrixRecyclerTextAdapter.getList())
        )
    }


    override fun setObservable()
    {
        matrixListenerViewModel.updateListener(this)
    }

    override fun showErrorDialog(
        errorBitmap: Bitmap,
        width: Float,
        height: Float,
        errorText: String
    )
    {
        Log.d("errorDialog@", "show dialog")
        dismissErrorDialog()
        errorDialogFragment =
            DefaultInputExceptionDialogFragment(errorBitmap, this, width, height, errorText)
        errorDialogFragment.show(childFragmentManager, "ERROR_DIALOG")
    }

    override fun showMatrixDialog(matrix: String, width: Float, height: Float, matrixBitmap: Bitmap)
    {
        fullEquationDialogFragment = FullEquationDialogFragment(
            matrix,
            matrixBitmap,
            width,
            height,
            this,
            firstMatrix,
            secondMatrix
        )
        fullEquationDialogFragment.show(
            childFragmentManager,
            "MATRIX_DIALOG"
        )
    }

    override fun btnMatrixDialogOkClicked()
    {
        mMatrixPresenter.onMatrixDialogBtnOkClicked()
    }

    override fun dismissMatrixDialog()
    {
        if (::fullEquationDialogFragment.isInitialized && fullEquationDialogFragment.isVisible) fullEquationDialogFragment.dismiss()
    }


    override fun btnOkPressed()
    {
        Log.d("errorDialog@", "btn ok pressed")
        mMatrixPresenter.onBtnOkInErrorDialogPressed()
    }

    override fun dismissErrorDialog()
    {
        Log.d("errorDialog@", "dismiss")
        if (::errorDialogFragment.isInitialized && errorDialogFragment.isVisible) errorDialogFragment.dismiss()
    }

    override fun getMaxSizeOfErrorDialog()
    {

        mMatrixPresenter.setMaxDialogSize(
            ltMatrixInput.measuredWidth.toFloat(),
            ltMatrixInput.measuredHeight.toFloat()
        )
    }

    override fun startLoadingInRecyclerView()
    {
        if (mMatrixRecyclerView.adapter is MatrixAdapterImageView)
        {
            Log.d("loading@", "start loading")
            mMatrixRecyclerImageAdapter.startLoading()
        }
    }

    override fun stopLoadingInRecyclerView()
    {
        if (mMatrixRecyclerView.adapter is MatrixAdapterImageView)
        {
            mMatrixRecyclerImageAdapter.stopLoading()
        }
    }

    override fun clearRecyclerView()
    {
        if (matrixRecycler.adapter is MatrixAdapterImageView) mMatrixRecyclerImageAdapter.clear()
        else mMatrixRecyclerTextAdapter.clear()
    }

    override fun setTopPosition()
    {
        scroll.scrollTo(0, 0)
    }

    override fun displayError(message: String)
    {
        DropSnackbar.make(matrixFrame).setMessage(message, color = Color.BLACK)
            .setProgressBar()
            .setDropInAnimation()
            .setBackground(CalculatorApplication.context.getDrawable(R.drawable.rectangle_error)!!)
            .setDuration(3000).show()
    }

    override fun startCalculation(matrixGroup: MatrixGroup)
    {
        if (matrixRecycler.adapter is MatrixAdapterImageView)
        {
            mMatrixRecyclerImageAdapter.addNewElem(matrixGroup)
            mMatrixRecyclerLayoutManager.scrollToPosition(0)
        }
    }

    override fun calculationFailed(matrixGroup: MatrixGroup)
    {
        if (matrixRecycler.adapter is MatrixAdapterImageView)
        {
            mMatrixRecyclerImageAdapter.removeCalculation(matrixGroup)
        }
    }

    override fun stopAllCalculations()
    {
        if (matrixRecycler.adapter is MatrixAdapterImageView)
        {
            mMatrixRecyclerImageAdapter.removeAllCalculation()
        }
    }

    override fun calculationCompleted(matrixGroup: MatrixGroup)
    {
        if (matrixRecycler.adapter is MatrixAdapterImageView)
        {
            mMatrixRecyclerImageAdapter.stopCalculation(matrixGroup)
        }
    }
}

