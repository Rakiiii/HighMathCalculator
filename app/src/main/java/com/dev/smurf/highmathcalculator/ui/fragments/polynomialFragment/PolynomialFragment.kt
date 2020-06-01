package com.dev.smurf.highmathcalculator.ui.fragments.polynomialFragment

import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dev.smurf.highmathcalculator.R
import com.dev.smurf.highmathcalculator.mvp.presenters.PolynomialPresenter
import com.dev.smurf.highmathcalculator.mvp.views.PolynomialViewInterface
import com.dev.smurf.highmathcalculator.ui.ViewModels.EditTextViewModel
import com.dev.smurf.highmathcalculator.ui.adapters.PolynomialAdapters.PolynomialAdapterImageView
import com.dev.smurf.highmathcalculator.ui.adapters.ViewPagersAdapters.BtnViewPagerFragmentStateAdapter
import com.dev.smurf.highmathcalculator.ui.fragments.fragmentInterfaces.Settingable
import com.example.smurf.mtarixcalc.PolynomialGroup
import com.example.smurf.mtarixcalc.PolynomialRecyclerViewModel
import com.example.smurf.mtarixcalc.PolynomialTxtAdapter
import com.example.smurf.mtarixcalc.SwipeToDeleteCallback
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_polinom.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import org.jetbrains.anko.toast

class PolynomialFragment : MvpAppCompatFragment(), PolynomialViewInterface, Settingable,
    PolynomialButtonsGridFirstPageFragment.OnFragmentInteractionListener
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
    private lateinit var mPolynomialRecyclerImageViewAdapter: PolynomialAdapterImageView
    private lateinit var mPolynomialRecyclerViewLayoutManager: LinearLayoutManager

    private lateinit var mBtnMatrixViewPagerAdapter: BtnViewPagerFragmentStateAdapter
    private var benFragmentSet = mutableListOf<Fragment>()

    private val mPolynomialRecyclerViewModel by viewModels<PolynomialRecyclerViewModel>()

    private val mPolynomialEditTextViewModel by viewModels<EditTextViewModel>()

    private var isPaused = false

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

        //инициализация recyclerView для полиномов
        initRecyclerView()

        //init view oager for button fragments
        initViewPager()
        //off view pager swap page by gesture
        buttonViewPagerPolynomail.isUserInputEnabled = false

        //добавление удаления свайпом
        enableSwipeToDeleteAndUndo()

        btnSwapPolynomial.setOnClickListener {
            val tmp = firstPolinom.text
            firstPolinom.text = secondPolinom.text
            secondPolinom.text = tmp
        }

    }


    override fun onResume()
    {
        Log.d("lifecycle@", "onResume")
        isPaused = false
        super.onResume()
    }

    override fun onPause()
    {
        Log.d("lifecycle@", "onPause")
        isPaused = true
        mPolynomialEditTextViewModel.firstValue = firstPolinom.text.toString()
        mPolynomialEditTextViewModel.secondValue = secondPolinom.text.toString()
        super.onPause()
    }


    private fun initRecyclerView()
    {
        mPolynomialRecyclerViewLayoutManager = LinearLayoutManager(this.context)

        mPolynomialRecyclerViewAdapter =
            PolynomialTxtAdapter(this.requireContext(), firstPolinom, secondPolinom)

        val point = Point()
        requireActivity().windowManager.defaultDisplay.getSize(point)

        val margin = 6 * requireContext().resources.displayMetrics.density

        mPolynomialRecyclerImageViewAdapter =
            PolynomialAdapterImageView(
                this.requireContext(),
                firstPolinom,
                secondPolinom,
                point.x.toFloat() - margin
            )

        mPolynomialRecyclerView = requireView().findViewById(R.id.polinomRecycler)

        mPolynomialRecyclerView.adapter = mPolynomialRecyclerViewAdapter

        mPolynomialRecyclerView.layoutManager = mPolynomialRecyclerViewLayoutManager
    }

    private fun enableSwipeToDeleteAndUndo()
    {
        val swipeToDeleteCallback = object : SwipeToDeleteCallback(this.requireContext())
        {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int)
            {

                if (mPolynomialRecyclerView.adapter !is PolynomialAdapterImageView)
                {
                    var isUnded = false

                    val position = viewHolder.absoluteAdapterPosition
                    val item = mPolynomialRecyclerViewAdapter.getData(position)

                    mPolynomialRecyclerViewAdapter.removeElement(position)


                    val snackbar = Snackbar
                        .make(polinomFrame, "Item was removed from the list.", Snackbar.LENGTH_LONG)
                    snackbar.setAction("UNDO")
                    {
                        mPolynomialRecyclerViewAdapter.restoreItem(item, position)
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
                    var isUnded = false

                    val position = viewHolder.absoluteAdapterPosition
                    val item = mPolynomialRecyclerImageViewAdapter.getData(position)

                    mPolynomialRecyclerImageViewAdapter.removeElement(position)


                    val snackbar = Snackbar
                        .make(polinomFrame, "Item was removed from the list.", Snackbar.LENGTH_LONG)
                    snackbar.setAction("UNDO")
                    {
                        mPolynomialRecyclerImageViewAdapter.restoreItem(item, position)
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
            }

        }

        val itemTouchhelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchhelper.attachToRecyclerView(mPolynomialRecyclerView)
    }


    override fun addToPolynomialRecyclerView(obj: PolynomialGroup)
    {
        if (mPolynomialRecyclerView.adapter !is PolynomialAdapterImageView)
            mPolynomialRecyclerViewAdapter.addElement(obj)
        else mPolynomialRecyclerImageViewAdapter.addElement(obj)
    }

    override fun showToast(obj: String)
    {
        this.requireContext().toast(obj)
    }

    override fun setRecyclerViewList(ar: MutableList<PolynomialGroup>)
    {
        if (mPolynomialRecyclerView.adapter !is PolynomialAdapterImageView)
        {
            mPolynomialRecyclerViewAdapter.setList(ar)
        }
        else
        {
            mPolynomialRecyclerImageViewAdapter.setList(ar)
        }
    }


    override fun setImageAdapter()
    {
        if (!isPaused && isRecycleViewInited())
        {
            mPolynomialRecyclerImageViewAdapter.setList(
                mPolynomialRecyclerViewAdapter.getList()
            )
            mPolynomialRecyclerView.adapter = mPolynomialRecyclerImageViewAdapter
            mPolynomialRecyclerImageViewAdapter.notifyDataSetChanged()
        }
    }

    override fun setTxtAdapter()
    {
        if (!isPaused && isRecycleViewInited())
        {
            mPolynomialRecyclerViewAdapter.setList(
                mPolynomialRecyclerImageViewAdapter.getList()
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
            if (mPolynomialRecyclerView.adapter !is PolynomialAdapterImageView) mPolynomialRecyclerViewAdapter.setList(
                mPolynomialRecyclerViewModel.getList()
            )
            else mPolynomialRecyclerImageViewAdapter.setList(
                mPolynomialRecyclerViewModel.getList()
            )
        }

        firstPolinom.text = SpannableStringBuilder(mPolynomialEditTextViewModel.firstValue)
        secondPolinom.text = SpannableStringBuilder(mPolynomialEditTextViewModel.secondValue)
    }

    override fun saveRecyclerViewToViewModel()
    {
        mPolynomialRecyclerViewModel.updateList(
            (if (mPolynomialRecyclerView.adapter is PolynomialAdapterImageView) mPolynomialRecyclerImageViewAdapter.getList()
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
                    requireActivity()
                )
            benFragmentSet.add(PolynomialButtonsGridFirstPageFragment().setListener(this))
            mBtnMatrixViewPagerAdapter.setNewFragmentSet(benFragmentSet)
            buttonViewPagerPolynomail.adapter = mBtnMatrixViewPagerAdapter
        }

    }

    private fun isRecycleViewInited() =
        (::mPolynomialRecyclerView.isInitialized &&
                ::mPolynomialRecyclerViewAdapter.isInitialized &&
                ::mPolynomialRecyclerImageViewAdapter.isInitialized &&
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
}



