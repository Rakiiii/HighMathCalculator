package com.dev.smurf.highmathcalculator.ui.fragments.matrixFragment

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
import com.dev.smurf.highmathcalculator.mvp.presenters.MatrixPresenter
import com.dev.smurf.highmathcalculator.mvp.views.MatrixViewInterface
import com.dev.smurf.highmathcalculator.ui.POJO.MatrixGroup
import com.dev.smurf.highmathcalculator.ui.ViewModels.EditTextViewModel
import com.dev.smurf.highmathcalculator.ui.adapters.ViewPagersAdapters.BtnViewPagerFragmentStateAdapter
import com.dev.smurf.highmathcalculator.ui.adapters.MatrixAdapters.MatrixAdapter
import com.dev.smurf.highmathcalculator.ui.adapters.MatrixAdapters.MatrixAdapterImageView
import com.dev.smurf.highmathcalculator.ui.fragments.fragmentInterfaces.Settingable
import com.example.smurf.mtarixcalc.MatrixRecyclerViewModel
import com.example.smurf.mtarixcalc.SwipeToDeleteCallback
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_matrix.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import org.jetbrains.anko.toast


class MatrixFragment : MvpAppCompatFragment(), MatrixViewInterface, Settingable,
    MatrixButtonGridFragmentFirstPage.onFragmentInteractionListener,
    MatrixButtonGridFragmentSecondPage.onFragmentInteractionListener
{

    @InjectPresenter
    lateinit var mMatrixPresenter: MatrixPresenter


    private lateinit var mMatrixRecyclerView: RecyclerView
    private lateinit var mMatrixRecyclerLayoutManager: LinearLayoutManager
    private lateinit var mMatrixRecyclerTextAdapter: MatrixAdapter
    private lateinit var mMatrixRecyclerImageAdapter: MatrixAdapterImageView
    private lateinit var mBtnMatrixViewPagerAdapter: BtnViewPagerFragmentStateAdapter

    private val mMatrixRecyclerViewModel by viewModels<MatrixRecyclerViewModel>()

    private val mMatrixEdittextViewModel by viewModels<EditTextViewModel>()

    private var isImageAdapter = false
    private var isPaused = false

    private var btnFragmentSet = mutableListOf<Fragment>()

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

        Log.d("lifecycle@", "onStart")

        //инициализация recycler view
        initRecyclerView()

        //initiali veapager with bottons
        initViewPager()

        //удаление и возврат по свайпу из recycler view
        enableSwipeToDeleteAndUndo()

        //off view pager swap page by gesture
        buttonViewPager.isUserInputEnabled = false

        //swap matrix btn listener
        btnSwap.setOnClickListener {
            val tmp = firstMatrix.text
            firstMatrix.text = secondMatrix.text
            secondMatrix.text = tmp
        }
    }


    override fun onResume()
    {
        isPaused = false
        Log.d("lifecycle@", "onResume")
        super.onResume()
    }


    override fun onPause()
    {
        isPaused = true
        Log.d("lifecycle@", "onPause")
        mMatrixEdittextViewModel.firstValue = firstMatrix.text.toString()
        mMatrixEdittextViewModel.secondValue = secondMatrix.text.toString()

        super.onPause()
    }


    //утсановка нового списка элементов для recycler view
    override fun setRecyclerViewArrayList(ar: ArrayList<MatrixGroup>)
    {
        if (!isImageAdapter)
        {
            mMatrixRecyclerTextAdapter.setList(ar.clone() as ArrayList<MatrixGroup>)
        }
        else
        {
            mMatrixRecyclerImageAdapter.setList(ar.clone() as ArrayList<MatrixGroup>)
        }

        mMatrixRecyclerViewModel.updateList(ar.clone() as ArrayList<MatrixGroup>)
    }


    //инициализация recyclerView
    private fun initRecyclerView()
    {
        if (isRecycleViewInitted()) return
        mMatrixRecyclerLayoutManager = LinearLayoutManager(context)

        mMatrixRecyclerTextAdapter =
            MatrixAdapter(
                requireContext(),
                firstMatrix,
                secondMatrix
            )
        val point = Point()
        requireActivity().windowManager.defaultDisplay.getSize(point)
        val margin =    46*requireContext().resources.displayMetrics.density
        Log.d("den@","densety is:"+requireContext().resources.displayMetrics.density.toString())
        mMatrixRecyclerImageAdapter =
            MatrixAdapterImageView(
                requireContext(),
                firstMatrix,
                secondMatrix,
                point.x.toFloat() - margin
            )

        mMatrixRecyclerView = requireView().findViewById(R.id.matrixRecycler)

        mMatrixRecyclerView.layoutManager = mMatrixRecyclerLayoutManager

        mMatrixRecyclerView.adapter = mMatrixRecyclerTextAdapter

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
            btnFragmentSet.add(MatrixButtonGridFragmentFirstPage().setListener(this))
            btnFragmentSet.add(MatrixButtonGridFragmentSecondPage().setListener(this))
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
        if (!isImageAdapter)
            mMatrixRecyclerTextAdapter.addNewElem(obj)
        else mMatrixRecyclerImageAdapter.addNewElem(obj)

    }

    override fun showToast(obj: String)
    {
        requireContext().toast(obj)
    }

    override fun setImageAdapter()
    {
        if (!isPaused && isRecycleViewInitted())
        {
            isImageAdapter = true
            mMatrixRecyclerImageAdapter.setList(
                mMatrixRecyclerTextAdapter.getList().map { it.Copy() }.toMutableList()
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
            isImageAdapter = false
            mMatrixRecyclerTextAdapter.setList(
                mMatrixRecyclerImageAdapter.getList().map { it.Copy() }.toMutableList()
            )
            mMatrixRecyclerView.adapter = mMatrixRecyclerTextAdapter
            mMatrixRecyclerTextAdapter.notifyDataSetChanged()
        }
    }


    //add swipe deletting behaviour
    private fun enableSwipeToDeleteAndUndo()
    {
        val swipeToDeleteCallback = object : SwipeToDeleteCallback(requireContext())
        {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int)
            {
                if (isImageAdapter)
                {
                    var isUnded = false


                    val position = viewHolder.absoluteAdapterPosition
                    val item = mMatrixRecyclerImageAdapter.getData(position)

                    mMatrixRecyclerImageAdapter.removeElement(position)


                    val snackbar = Snackbar
                        .make(matrixFrame, "Item was removed from the list.", Snackbar.LENGTH_LONG)
                    snackbar.setAction("UNDO")
                    {
                        mMatrixRecyclerImageAdapter.restoreItem(position, item)
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
            if (isImageAdapter) mMatrixRecyclerImageAdapter.setList(
                mMatrixRecyclerViewModel.getList()
            )
            else mMatrixRecyclerTextAdapter.setList(
                mMatrixRecyclerViewModel.getList()
            )
        }
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
        mMatrixPresenter.btnEighnvalueClicked()
    }

    override fun btnEighnvectorClicked()
    {
        mMatrixPresenter.btnEighnvectorClicked()
    }

    override fun btnNegativeClicked()
    {
        mMatrixPresenter.btnNegativeClicked()
    }

    override fun btnPositiveClicked()
    {
        mMatrixPresenter.btnPositiveClicked()
    }

    override fun btnRankClicked()
    {
        mMatrixPresenter.btnRankClicked()
    }

    override fun btnSolveSystemClicked()
    {
        mMatrixPresenter.btnSolveSystemClicked()
    }

    override fun btnSwitchSPClicked()
    {
        mMatrixPresenter.btnSwitchClicked(0)
    }


    override fun saveListRecyclerViewViewModel()
    {
        if (isImageAdapter)
        {
            val result = mMatrixRecyclerImageAdapter.getList()
                .map {
                    it.copy(
                        leftMatrix = it.leftMatrix,
                        rightMatrix = it.rightMatrix,
                        sign = it.sign,
                        resMatrix = it.resMatrix,
                        time = it.time
                    )
                }
            mMatrixRecyclerViewModel.updateList(result.toMutableList())
        }
        else
        {
            val result = mMatrixRecyclerTextAdapter.getList()
                .map {
                    it.copy(
                        leftMatrix = it.leftMatrix,
                        rightMatrix = it.rightMatrix,
                        sign = it.sign,
                        resMatrix = it.resMatrix,
                        time = it.time
                    )
                }
            mMatrixRecyclerViewModel.updateList(result.toMutableList())
        }
    }


}

