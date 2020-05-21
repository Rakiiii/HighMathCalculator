package com.dev.smurf.highmathcalculator.ui.fragments.matrixFragment

import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dev.smurf.highmathcalculator.R
import com.dev.smurf.highmathcalculator.mvp.presenters.MatrixPresenter
import com.dev.smurf.highmathcalculator.mvp.views.MatrixViewInterface
import com.dev.smurf.highmathcalculator.ui.ViewModels.EditTextViewModel
import com.dev.smurf.highmathcalculator.ui.adapters.ViewPagersAdapters.BtnViewPagerFragmentStateAdapter
import com.dev.smurf.highmathcalculator.ui.adapters.MatrixAdapters.MatrixAdapter
import com.dev.smurf.highmathcalculator.ui.adapters.MatrixAdapters.MatrixAdapterImageView
import com.dev.smurf.highmathcalculator.ui.fragments.fragmentInterfaces.Settingable
import com.example.smurf.mtarixcalc.MatrixGroup
import com.example.smurf.mtarixcalc.MatrixRecyclerViewModel
import com.example.smurf.mtarixcalc.SwipeToDeleteCallback
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_matrix.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import org.jetbrains.anko.toast


class MatrixFragment : MvpAppCompatFragment(), MatrixViewInterface, Settingable,
    MatrixButtonGridFragment.onFragmentInteractionListener,
        MatrixButtonGridFragmentSecondPage.onFragmentInteractionListener
{

    private var loaded: Boolean = false

    @InjectPresenter
    lateinit var mMatrixPresenter: MatrixPresenter


    private lateinit var mMatrixRecyclerView: RecyclerView
    private lateinit var mMatrixRecyclerLayoutManager: LinearLayoutManager
    private lateinit var mMatrixRecyclerTextAdapter: MatrixAdapter
    private lateinit var mMatrixRecyclerImageAdapter: MatrixAdapterImageView
    private lateinit var mBtnMatrixViewPagerAdapter: BtnViewPagerFragmentStateAdapter

    private lateinit var mMatrixRecyclerViewModel: MatrixRecyclerViewModel

    private lateinit var mMatrixEdittextViewModel: EditTextViewModel

    private var isImageAdapter = false
    private var isPaused = false

    private var BtnFrgmentSet = mutableListOf<Fragment>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_matrix, container, false)

    }

    override fun onStart()
    {
        super.onStart()

        //view model для сохранения содержимого recycler view
        mMatrixRecyclerViewModel =
            ViewModelProviders.of(this.activity!!).get(MatrixRecyclerViewModel::class.java)

        //инициализация view model для содержимого edittext
        mMatrixEdittextViewModel =
            ViewModelProviders.of(activity as FragmentActivity).get(EditTextViewModel::class.java)

        //инициализация recycler view
        initRecyclerView()

        //initiali veapager with bottons
        initViewPager()

        //удаление и возврат по свайпу из recycler view
        enableSwipeToDeleteAndUndo()

        //off view pager swap page by gesture
        buttonViewPager.isUserInputEnabled=false

        //swap matrix btn listener
        btnSwap.setOnClickListener {
            val tmp = firstMatrix.text
            firstMatrix.text = secondMatrix.text
            secondMatrix.text = tmp
        }

        //get data from view models
        restoreFromViewModel()

        //load data from db
        loadData()

    }

    override fun onResume()
    {
        isPaused = false
        super.onResume()
        updateSettings()
        restoreFromViewModel()
    }


    override fun onPause()
    {
        isPaused = true
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
        mMatrixRecyclerLayoutManager = LinearLayoutManager(context)

        mMatrixRecyclerTextAdapter =
            MatrixAdapter(
                context!!,
                firstMatrix,
                secondMatrix
            )
        val point = Point()
        activity!!.windowManager.defaultDisplay.getSize(point)
        mMatrixRecyclerImageAdapter =
            MatrixAdapterImageView(
                context!!,
                firstMatrix,
                secondMatrix,
                point.x.toFloat()
            )

        mMatrixRecyclerView = view!!.findViewById(R.id.matrixRecycler)

        mMatrixRecyclerView.layoutManager = mMatrixRecyclerLayoutManager

        mMatrixRecyclerView.adapter = mMatrixRecyclerTextAdapter

    }

    private fun initViewPager()
    {
        if (activity != null)
        {
            mBtnMatrixViewPagerAdapter =
                BtnViewPagerFragmentStateAdapter(
                    activity!!
                )
            BtnFrgmentSet.add(MatrixButtonGridFragment().addEventListener(this))
            BtnFrgmentSet.add(MatrixButtonGridFragmentSecondPage().setListener(this))
            mBtnMatrixViewPagerAdapter.setNewFragmentSet(BtnFrgmentSet)
            buttonViewPager.adapter = mBtnMatrixViewPagerAdapter
        }

    }

    override fun addToRecyclerView(obj: MatrixGroup)
    {
        if (!isImageAdapter)
            mMatrixRecyclerTextAdapter.addNewElem(
                mMatrixRecyclerViewModel.add(obj)
            )
        else mMatrixRecyclerImageAdapter.addNewElem(
            mMatrixRecyclerViewModel.add(obj)
        )

    }

    override fun showToast(obj: String)
    {
        context!!.toast(obj)
    }

    private fun setImageAdapter()
    {
        if (!isPaused && isRecycleViewInitted())
        {
            isImageAdapter = true
            mMatrixRecyclerImageAdapter.setList(
                mMatrixRecyclerTextAdapter.getList().clone() as ArrayList<MatrixGroup>
            )
            mMatrixRecyclerView.adapter = mMatrixRecyclerImageAdapter
            mMatrixRecyclerImageAdapter.notifyDataSetChanged()
        }
    }

    private fun setTextAdapter()
    {
        if (!isPaused && isRecycleViewInitted())
        {
            isImageAdapter = false
            mMatrixRecyclerTextAdapter.setList(
                mMatrixRecyclerImageAdapter.getList().clone() as ArrayList<MatrixGroup>
            )
            mMatrixRecyclerView.adapter = mMatrixRecyclerTextAdapter
            mMatrixRecyclerTextAdapter.notifyDataSetChanged()
        }
    }


    private fun enableSwipeToDeleteAndUndo()
    {
        val swipeToDeleteCallback = object : SwipeToDeleteCallback(context!!)
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

    private fun loadData()
    {
        if (!loaded)
        {
            mMatrixPresenter.onLoadSavedInstance()
            loaded = true
        }
    }

    override fun updateSettings()
    {
        if (mMatrixPresenter.checkImageMode())
        {
            setImageAdapter()
        }
        else
        {
            if (isImageAdapter)
            {
                setTextAdapter()
            }
        }
    }

    private fun restoreFromViewModel()
    {
        //востонавливаемся из view model
        if (!mMatrixRecyclerViewModel.isEmpty())
        {
            if (isImageAdapter) mMatrixRecyclerImageAdapter.setList(
                mMatrixRecyclerViewModel.getList().clone()
                        as ArrayList<MatrixGroup>
            )
            else mMatrixRecyclerTextAdapter.setList(
                mMatrixRecyclerViewModel.getList().clone()
                        as ArrayList<MatrixGroup>
            )
        }
        firstMatrix.text = SpannableStringBuilder(mMatrixEdittextViewModel.firstValue)
        secondMatrix.text = SpannableStringBuilder(mMatrixEdittextViewModel.secondValue)
    }

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

    override fun setBtnFragment(position : Int)
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
}

