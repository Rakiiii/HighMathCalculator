package com.dev.smurf.highmathcalculator.ui.fragments

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.dev.smurf.highmathcalculator.R
import com.dev.smurf.highmathcalculator.mvp.presenters.MatrixPresenter
import com.dev.smurf.highmathcalculator.mvp.views.MatrixViewInterface
import com.example.smurf.mtarixcalc.MatrixRecyclerViewModel
import com.example.smurf.mtarixcalc.SwipeToDeleteCallback
import com.example.smurf.mtarixcalc.matrixAdapter
import com.example.smurf.mtarixcalc.matrixGroup
import kotlinx.android.synthetic.main.fragment_matrix.*


class MatrixFragment : MvpFragment(),MatrixViewInterface
{
    private var listener: OnFragmentInteractionListener? = null

    @InjectPresenter
    lateinit var mMatrixPresenter: MatrixPresenter


    private lateinit var matrixRecycler : RecyclerView
    private lateinit var matrixRecyclerLayoutManager: LinearLayoutManager
    private lateinit var matrixRecyclerAdapter: matrixAdapter

    private lateinit var mMatrixRecyclerViewModel : MatrixRecyclerViewModel

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
        mMatrixRecyclerViewModel = ViewModelProviders.of(activity as FragmentActivity).get(MatrixRecyclerViewModel::class.java)

        //инициализация recycler view
        initRecyclerView()

        //удаление и возврат по свайпу из recycler view
        enableSwipeToDeleteAndUndo()

        //слушатель для кнопки плюс
        btnPlus.setOnClickListener { v ->
            mMatrixPresenter.onPlusClick(
                firstMatrix.text.toString(),
                secondMatrix.text.toString()
            )
        }

        //слушатель для кнопки минус
        btnMinus.setOnClickListener { v ->
            mMatrixPresenter.onMinusClick(
                firstMatrix.text.toString(),
                secondMatrix.text.toString()
            )
        }

        //слушатель для кнопки умножения первой матрицы на вторую
        btnFirstTimesSecond.setOnClickListener { v ->
            mMatrixPresenter.onTimesClick(firstMatrix.text.toString(), secondMatrix.text.toString())
        }

        //случатель для кнопки умножения второй матрицы на первую
        btnSecondTimesFirst.setOnClickListener { v ->
            mMatrixPresenter.onTimesClick(secondMatrix.text.toString(), firstMatrix.text.toString())
        }

        //слушатель для инвертированния первой матрицы
        btnInvers.setOnClickListener { v -> mMatrixPresenter.onInversClick(firstMatrix.text.toString()) }

        //слушатель для поиска определителя пераой матрицы
        btnDeterminant.setOnClickListener { v -> mMatrixPresenter.onDeterminantClick(firstMatrix.text.toString()) }

        //востонавливаемся из view model
        if(!mMatrixRecyclerViewModel.isEmpty())matrixRecyclerAdapter.setList(mMatrixRecyclerViewModel.getList())
    }



    override fun onAttach(context: Context)
    {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener)
        {
            listener = context
        } else
        {
            //throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach()
    {
        super.onDetach()
        listener = null
    }


    interface OnFragmentInteractionListener
    {
        fun onFragmentInteraction(uri: Uri)
    }

    //инициализация recyclerView
    private fun initRecyclerView()
    {
        matrixRecyclerLayoutManager = LinearLayoutManager(context)


        matrixRecyclerAdapter =  matrixAdapter(context , firstMatrix , secondMatrix)

        matrixRecycler = view.findViewById( R.id.matrixRecycler)

        matrixRecycler.layoutManager = matrixRecyclerLayoutManager

        matrixRecycler.adapter = matrixRecyclerAdapter

    }

    override fun addToRecyclerView(obj: matrixGroup)
    {
        matrixRecyclerAdapter.addNewElem(mMatrixRecyclerViewModel.add(obj))

    }

    private fun enableSwipeToDeleteAndUndo()
    {
        val swipeToDeleteCallback = object : SwipeToDeleteCallback(context) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {


                val position = viewHolder.adapterPosition
                val item = matrixRecyclerAdapter.getData(position)

                matrixRecyclerAdapter.removeElement(position)


                val snackbar = Snackbar
                    .make( matrixFrame , "Item was removed from the list.", Snackbar.LENGTH_LONG)
                snackbar.setAction("UNDO") {
                    matrixRecyclerAdapter.restoreItem(position , item)
                    matrixRecycler.scrollToPosition(position)
                }

                snackbar.setActionTextColor(Color.YELLOW)
                snackbar.show()

                mMatrixRecyclerViewModel.updateList(matrixRecyclerAdapter.getList())

            }
        }

        val itemTouchhelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchhelper.attachToRecyclerView(matrixRecycler)
    }

}
