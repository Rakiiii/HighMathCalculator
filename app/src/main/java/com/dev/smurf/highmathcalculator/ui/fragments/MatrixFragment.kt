package com.dev.smurf.highmathcalculator.ui.fragments

//import android.support.design.widget.Snackbar
//import androidx.fragment.app.FragmentActivity
//import androidx.appcompat.widget.LinearLayoutManager
//import androidx.appcompat.widget.RecyclerView
//import androidx.appcompat.widget.helper.ItemTouchHelper
//import com.arellomobile.mvp.MvpFragment
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.dev.smurf.highmathcalculator.R
import com.dev.smurf.highmathcalculator.mvp.presenters.MatrixPresenter
import com.dev.smurf.highmathcalculator.mvp.views.MatrixViewInterface
import com.example.smurf.mtarixcalc.MatrixGroup
import com.example.smurf.mtarixcalc.MatrixRecyclerViewModel
import com.example.smurf.mtarixcalc.SwipeToDeleteCallback
import com.example.smurf.mtarixcalc.matrixAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_matrix.*
import org.jetbrains.anko.toast


class MatrixFragment : com.dev.smurf.highmathcalculator.moxyTmpAMdroisdXSupport.MvpAppCompatFragment(),MatrixViewInterface
{
    private var listener: OnFragmentInteractionListener? = null

    private var loaded : Boolean = false

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

        if(!loaded)
        {
            mMatrixPresenter.onLoadSavedInstance()
            loaded = true
        }
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

    //утсановка нового списка элементов для recycler view
    override fun setRecyclerViewArrayList(ar: ArrayList<MatrixGroup>)
    {
        matrixRecyclerAdapter.setList(ar)
        mMatrixRecyclerViewModel.updateList(ar)
    }


    interface OnFragmentInteractionListener
    {
        fun onFragmentInteraction(uri: Uri)
    }

    //инициализация recyclerView
    private fun initRecyclerView()
    {
        matrixRecyclerLayoutManager = LinearLayoutManager(context)


        matrixRecyclerAdapter =  matrixAdapter(context!! , firstMatrix , secondMatrix)

        matrixRecycler = view!!.findViewById( R.id.matrixRecycler)

        matrixRecycler.layoutManager = matrixRecyclerLayoutManager

        matrixRecycler.adapter = matrixRecyclerAdapter

    }

    override fun addToRecyclerView(obj: MatrixGroup)
    {
        matrixRecyclerAdapter.addNewElem(mMatrixRecyclerViewModel.add(obj))

    }

    override fun showToast(obj: String)
    {
        this.context!!.toast(obj)
    }

    private fun enableSwipeToDeleteAndUndo()
    {
        val swipeToDeleteCallback = object : SwipeToDeleteCallback(context!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {

                var isUnded = false

                val position = viewHolder.adapterPosition
                val item = matrixRecyclerAdapter.getData(position)

                matrixRecyclerAdapter.removeElement(position)


                val snackbar = Snackbar
                    .make( matrixFrame , "Item was removed from the list.", Snackbar.LENGTH_LONG)
                snackbar.setAction("UNDO") {
                    matrixRecyclerAdapter.restoreItem(position , item)
                    isUnded = true
                    matrixRecycler.scrollToPosition(position)
                }

                snackbar.setActionTextColor(Color.YELLOW)
                snackbar.show()

                mMatrixRecyclerViewModel.updateList(matrixRecyclerAdapter.getList())
                if(!isUnded)mMatrixPresenter.deleteFromDb(item)

            }
        }

        val itemTouchhelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchhelper.attachToRecyclerView(matrixRecycler)
    }
}
