package com.dev.smurf.highmathcalculator.ui.fragments.matrixFragment

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dev.smurf.highmathcalculator.R
import com.dev.smurf.highmathcalculator.mvp.presenters.MatrixPresenter
import com.dev.smurf.highmathcalculator.mvp.views.MatrixViewInterface
import com.dev.smurf.highmathcalculator.ui.ViewModels.EditTextViewModel
import com.dev.smurf.highmathcalculator.ui.adapters.MatrixAdapter
import com.dev.smurf.highmathcalculator.ui.adapters.MatrixAdapterImageView
import com.dev.smurf.highmathcalculator.ui.fragments.fragmentInterfaces.Settingable
import com.example.smurf.mtarixcalc.MatrixGroup
import com.example.smurf.mtarixcalc.MatrixRecyclerViewModel
import com.example.smurf.mtarixcalc.SwipeToDeleteCallback
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_matrix.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import org.jetbrains.anko.toast


class MatrixFragment : MvpAppCompatFragment(), MatrixViewInterface, Settingable
{
    private var listener: OnFragmentInteractionListener? = null

    private var loaded: Boolean = false

    //buffer for toast, because amount off ui threads is finite
    val Toasts = MutableList(0) { "" }
    val toastHandler = Handler()

    @InjectPresenter
    lateinit var mMatrixPresenter: MatrixPresenter


    private lateinit var matrixRecycler: RecyclerView
    private lateinit var matrixRecyclerLayoutManager: LinearLayoutManager
    private lateinit var matrixRecyclerTextAdapter: MatrixAdapter
    private lateinit var matrixRecyclerImageAdapter: MatrixAdapterImageView

    private lateinit var mMatrixRecyclerViewModel: MatrixRecyclerViewModel

    private lateinit var mMatrixEdittextViewModel: EditTextViewModel

    private var isImageAdapter = false

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
        mMatrixRecyclerViewModel = ViewModelProviders.of(this.activity!!).get(MatrixRecyclerViewModel::class.java)

        //инициализация view model для содержимого edittext
        mMatrixEdittextViewModel =
            ViewModelProviders.of(activity as FragmentActivity).get(EditTextViewModel::class.java)

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

        btnSwap.setOnClickListener {
            val tmp = firstMatrix.text
            firstMatrix.text = secondMatrix.text
            secondMatrix.text = tmp
        }

        //востонавливаемся из view model
        if (!mMatrixRecyclerViewModel.isEmpty()) matrixRecyclerTextAdapter.setList(mMatrixRecyclerViewModel.getList().clone() as ArrayList<MatrixGroup>)

        firstMatrix.text = SpannableStringBuilder(mMatrixEdittextViewModel.firstValue)
        secondMatrix.text = SpannableStringBuilder(mMatrixEdittextViewModel.secondValue)

        if (!loaded)
        {
            mMatrixPresenter.onLoadSavedInstance()
            loaded = true
        }

        checkToast()

    }

    override fun onResume()
    {
        super.onResume()

        updateSettings()

    }


    override fun onAttach(context: Context)
    {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener)
        {
            listener = context
        }
    }

    override fun onDetach()
    {
        super.onDetach()
        listener = null
    }

    override fun onStop()
    {
        mMatrixEdittextViewModel.firstValue = firstMatrix.text.toString()
        mMatrixEdittextViewModel.secondValue = secondMatrix.text.toString()

        super.onStop()

    }

    //утсановка нового списка элементов для recycler view
    override fun setRecyclerViewArrayList(ar: ArrayList<MatrixGroup>)
    {
        if (!isImageAdapter)
        {
            matrixRecyclerTextAdapter.setList(ar.clone() as ArrayList<MatrixGroup>)
        }
        else
        {
            matrixRecyclerImageAdapter.setList(ar.clone() as ArrayList<MatrixGroup>)
        }

        mMatrixRecyclerViewModel.updateList(ar.clone() as ArrayList<MatrixGroup>)
    }


    interface OnFragmentInteractionListener
    {
        fun onFragmentInteraction(uri: Uri)
    }

    //инициализация recyclerView
    private fun initRecyclerView()
    {
        matrixRecyclerLayoutManager = LinearLayoutManager(context)

        matrixRecyclerTextAdapter = MatrixAdapter(context!!, firstMatrix, secondMatrix)

        matrixRecyclerImageAdapter = MatrixAdapterImageView(context!!, firstMatrix, secondMatrix)

        matrixRecycler = view!!.findViewById(R.id.matrixRecycler)

        matrixRecycler.layoutManager = matrixRecyclerLayoutManager

        matrixRecycler.adapter = matrixRecyclerTextAdapter

    }

    override fun addToRecyclerView(obj: MatrixGroup)
    {
        if (!isImageAdapter)
            matrixRecyclerTextAdapter.addNewElem(
                mMatrixRecyclerViewModel.add(obj)
            )
        else matrixRecyclerImageAdapter.addNewElem(
            mMatrixRecyclerViewModel.add(obj)
        )

    }

    override fun showToast(obj: String)
    {
        Toasts.add(obj)
    }

    fun setImageAdapter()
    {
        matrixRecyclerImageAdapter.setList(matrixRecyclerTextAdapter.getList().clone() as ArrayList<MatrixGroup>)
        matrixRecycler.swapAdapter(matrixRecyclerImageAdapter, true)
        matrixRecyclerImageAdapter.notifyDataSetChanged()
    }

    fun setTextAdapter()
    {
        matrixRecyclerTextAdapter.setList(matrixRecyclerImageAdapter.getList().clone() as ArrayList<MatrixGroup>)
        matrixRecycler.adapter = matrixRecyclerTextAdapter
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

                    val position = viewHolder.adapterPosition
                    val item = matrixRecyclerImageAdapter.getData(position)

                    matrixRecyclerImageAdapter.removeElement(position)


                    val snackbar = Snackbar
                        .make(matrixFrame, "Item was removed from the list.", Snackbar.LENGTH_LONG)
                    snackbar.setAction("UNDO")
                    {
                        matrixRecyclerImageAdapter.restoreItem(position, item)
                        isUnded = true
                        matrixRecycler.scrollToPosition(position)
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

                    val position = viewHolder.adapterPosition
                    val item = matrixRecyclerTextAdapter.getData(position)

                    matrixRecyclerTextAdapter.removeElement(position)


                    val snackbar = Snackbar
                        .make(matrixFrame, "Item was removed from the list.", Snackbar.LENGTH_LONG)
                    snackbar.setAction("UNDO")
                    {
                        matrixRecyclerTextAdapter.restoreItem(position, item)
                        isUnded = true
                        matrixRecycler.scrollToPosition(position)
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
        itemTouchhelper.attachToRecyclerView(matrixRecycler)
    }

    fun checkToast()
    {
        if (Toasts.isNotEmpty())
        {
            for (i in Toasts)
            {
                this.context!!.toast(i)
            }
            Toasts.clear()
        }

        toastHandler.postDelayed({
            checkToast()
        }, 2000)
    }

    override fun updateSettings()
    {
        if (mMatrixPresenter.checkImageMode())
        {
            isImageAdapter = true
            setImageAdapter()
        }
        else
        {
            if (isImageAdapter)
            {
                setTextAdapter()
                isImageAdapter = false
            }
        }
    }
}
