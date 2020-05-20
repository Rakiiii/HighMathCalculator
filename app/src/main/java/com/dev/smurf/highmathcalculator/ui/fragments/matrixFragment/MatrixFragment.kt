package com.dev.smurf.highmathcalculator.ui.fragments.matrixFragment

import android.graphics.Color
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

    private var loaded: Boolean = false

    //buffer for toast, because amount off ui threads is finite
    val Toasts = MutableList(0) { "" }
    val toastHandler = Handler()

    @InjectPresenter
    lateinit var mMatrixPresenter: MatrixPresenter


    private lateinit var mMatrixRecyclerView: RecyclerView
    private lateinit var mMatrixRecyclerLayoutManager: LinearLayoutManager
    private lateinit var mMatrixRecyclerTextAdapter: MatrixAdapter
    private lateinit var mMatrixRecyclerImageAdapter: MatrixAdapterImageView

    private lateinit var mMatrixRecyclerViewModel: MatrixRecyclerViewModel

    private lateinit var mMatrixEdittextViewModel: EditTextViewModel

    private var isImageAdapter = false
    private var isPaused = false

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

        restoreFromViewModel()

        if (!loaded)
        {
            mMatrixPresenter.onLoadSavedInstance()
            loaded = true
        }

        checkToast()

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

        mMatrixRecyclerTextAdapter = MatrixAdapter(context!!, firstMatrix, secondMatrix)

        mMatrixRecyclerImageAdapter = MatrixAdapterImageView(context!!, firstMatrix, secondMatrix)

        mMatrixRecyclerView = view!!.findViewById(R.id.matrixRecycler)

        mMatrixRecyclerView.layoutManager = mMatrixRecyclerLayoutManager

        mMatrixRecyclerView.adapter = mMatrixRecyclerTextAdapter

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
        Toasts.add(obj)
    }

    fun setImageAdapter()
    {
        if (!isPaused && isRecycleViewInited())
        {
            isImageAdapter = true
            mMatrixRecyclerImageAdapter.setList(mMatrixRecyclerTextAdapter.getList().clone() as ArrayList<MatrixGroup>)
            mMatrixRecyclerView.adapter = mMatrixRecyclerImageAdapter
            mMatrixRecyclerImageAdapter.notifyDataSetChanged()
        }
    }

    fun setTextAdapter()
    {
        if (!isPaused && isRecycleViewInited())
        {
            isImageAdapter = false
            mMatrixRecyclerTextAdapter.setList(mMatrixRecyclerImageAdapter.getList().clone() as ArrayList<MatrixGroup>)
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

                    val position = viewHolder.adapterPosition
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

                    val position = viewHolder.adapterPosition
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

    private fun isRecycleViewInited() =
        (::mMatrixRecyclerView.isInitialized &&
                ::mMatrixRecyclerLayoutManager.isInitialized &&
                ::mMatrixRecyclerImageAdapter.isInitialized &&
                ::mMatrixRecyclerTextAdapter.isInitialized
                )

}
