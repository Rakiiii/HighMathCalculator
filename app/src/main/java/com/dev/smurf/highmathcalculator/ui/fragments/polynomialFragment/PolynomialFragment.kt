package com.dev.smurf.highmathcalculator.ui.fragments.polynomialFragment

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
import com.dev.smurf.highmathcalculator.mvp.presenters.PolynomialPresenter
import com.dev.smurf.highmathcalculator.mvp.views.PolynomialViewInterface
import com.dev.smurf.highmathcalculator.ui.ViewModels.EditTextViewModel
import com.dev.smurf.highmathcalculator.ui.adapters.PolynomialAdapterImageView
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

class PolynomialFragment : MvpAppCompatFragment(), PolynomialViewInterface, Settingable
{

    //buffer for toast, because amount off ui threads is finite
    val Toasts = MutableList(0) { "" }
    val toastHandler = Handler()

    //вставляем презентер
    @InjectPresenter
    lateinit var mPolynomialPresenter: PolynomialPresenter

    init
    {
        mvpDelegate.onCreate()
        mvpDelegate.onAttach()
    }


    //recycler view для полиномов
    private lateinit var mPolinomRecyclerView: RecyclerView
    private lateinit var mPolinomRecyclerViewAdapter: PolynomialTxtAdapter
    private lateinit var mPolinomRecyclerImageViewAdapter: PolynomialAdapterImageView
    private lateinit var mPolinomRecyclerViewLayoutManager: LinearLayoutManager

    private lateinit var mPolynomialRecyclerViewModel: PolynomialRecyclerViewModel

    private lateinit var mPolinomEditTextViewModel: EditTextViewModel

    private var isLoaded: Boolean = false
    private var isImageViewHolder = false
    private var isPaused = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_polinom, container, false)
    }

    override fun onStart()
    {
        super.onStart()

        //инициализация viewModel для полиномов
        mPolynomialRecyclerViewModel =
            ViewModelProviders.of(activity as FragmentActivity).get(PolynomialRecyclerViewModel::class.java)

        //инициализация view model для содержимого EditText
        mPolinomEditTextViewModel =
            ViewModelProviders.of(activity as FragmentActivity).get(EditTextViewModel::class.java)

        //инициализация recyclerView для полиномов
        initRecyclerView()

        //добавление удаления свайпом
        enableSwipeToDeleteAndUndo()


        //добовляем обработчики
        btnPolPlus.setOnClickListener {
            mPolynomialPresenter.onPlusClick(firstPolinom.text.toString(), secondPolinom.text.toString())
        }

        btnPolMinus.setOnClickListener {
            mPolynomialPresenter.onMinusClick(firstPolinom.text.toString(), secondPolinom.text.toString())
        }

        btnPolTimes.setOnClickListener {
            mPolynomialPresenter.onTimesClick(firstPolinom.text.toString(), secondPolinom.text.toString())
        }

        btnPolDiv.setOnClickListener {
            mPolynomialPresenter.onDivisionClick(firstPolinom.text.toString(), secondPolinom.text.toString())
        }

        btnPolRootsA.setOnClickListener {
            mPolynomialPresenter.onRootsOfClick(firstPolinom.text.toString())
        }

        btnPolRootsB.setOnClickListener {
            mPolynomialPresenter.onRootsOfClick(secondPolinom.text.toString())
        }

        btnSwapPolynomial.setOnClickListener {
            val tmp = firstPolinom.text
            firstPolinom.text = secondPolinom.text
            secondPolinom.text = tmp
        }

        restoreFromViewModel()

        if (!isLoaded)
        {
            mPolynomialPresenter.onLoadSavedInstance()
            isLoaded = true
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
        mPolinomEditTextViewModel.firstValue = firstPolinom.text.toString()
        mPolinomEditTextViewModel.secondValue = secondPolinom.text.toString()
        super.onPause()
    }


    fun initRecyclerView()
    {
        mPolinomRecyclerViewLayoutManager = LinearLayoutManager(this.context)

        mPolinomRecyclerViewAdapter = PolynomialTxtAdapter(this.context!!, firstPolinom, secondPolinom)

        mPolinomRecyclerImageViewAdapter = PolynomialAdapterImageView(this.context!!, firstPolinom, secondPolinom)

        mPolinomRecyclerView = view!!.findViewById(R.id.polinomRecycler)

        mPolinomRecyclerView.adapter = mPolinomRecyclerViewAdapter

        mPolinomRecyclerView.layoutManager = mPolinomRecyclerViewLayoutManager
    }

    private fun enableSwipeToDeleteAndUndo()
    {
        val swipeToDeleteCallback = object : SwipeToDeleteCallback(this.context!!)
        {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int)
            {

                if (!isImageViewHolder)
                {
                    var isUnded = false

                    val position = viewHolder.adapterPosition
                    val item = mPolinomRecyclerViewAdapter.getData(position)

                    mPolinomRecyclerViewAdapter.removeElement(position)


                    val snackbar = Snackbar
                        .make(polinomFrame, "Item was removed from the list.", Snackbar.LENGTH_LONG)
                    snackbar.setAction("UNDO")
                    {
                        mPolinomRecyclerViewAdapter.restoreItem(item, position)
                        isUnded = true
                        mPolinomRecyclerView.scrollToPosition(position)
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

                    val position = viewHolder.adapterPosition
                    val item = mPolinomRecyclerImageViewAdapter.getData(position)

                    mPolinomRecyclerImageViewAdapter.removeElement(position)


                    val snackbar = Snackbar
                        .make(polinomFrame, "Item was removed from the list.", Snackbar.LENGTH_LONG)
                    snackbar.setAction("UNDO")
                    {
                        mPolinomRecyclerImageViewAdapter.restoreItem(item, position)
                        isUnded = true
                        mPolinomRecyclerView.scrollToPosition(position)
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
        itemTouchhelper.attachToRecyclerView(mPolinomRecyclerView)
    }


    override fun addToPolynomialRecyclerView(obj: PolynomialGroup)
    {
        if (!isImageViewHolder)
            mPolinomRecyclerViewAdapter.addElement(mPolynomialRecyclerViewModel.add(obj))
        else mPolinomRecyclerImageViewAdapter.addElement(mPolynomialRecyclerViewModel.add(obj))
    }

    override fun showToast(obj: String)
    {
        Toasts.add(obj)
        //this.context!!.toast(obj)
    }

    override fun setRecyclerViewList(ar: ArrayList<PolynomialGroup>)
    {
        if (!isImageViewHolder)
        {
            mPolinomRecyclerViewAdapter.setList(ar.clone() as ArrayList<PolynomialGroup>)
        }
        else
        {
            mPolinomRecyclerImageViewAdapter.setList(ar.clone() as ArrayList<PolynomialGroup>)
        }
        mPolynomialRecyclerViewModel.updateList(ar.clone() as ArrayList<PolynomialGroup>)
    }


    fun setImageAdapter()
    {
        if (!isPaused && isRecycleViewInited())
        {
            isImageViewHolder = true
            mPolinomRecyclerImageViewAdapter.setList(mPolinomRecyclerViewAdapter.getList().clone() as ArrayList<PolynomialGroup>)
            mPolinomRecyclerView.adapter = mPolinomRecyclerImageViewAdapter
            mPolinomRecyclerImageViewAdapter.notifyDataSetChanged()
        }
    }

    fun setTxtAdapter()
    {
        if (!isPaused && isRecycleViewInited())
        {
            isImageViewHolder = false
            mPolinomRecyclerViewAdapter.setList(mPolinomRecyclerImageViewAdapter.getList().clone() as ArrayList<PolynomialGroup>)
            mPolinomRecyclerView.adapter = mPolinomRecyclerViewAdapter
            mPolinomRecyclerViewAdapter.notifyDataSetChanged()
        }
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
        if (mPolynomialPresenter.checkImageMode())
        {
            setImageAdapter()
        }
        else
        {
            if (isImageViewHolder)
            {
                setTxtAdapter()
            }
        }
    }

    private fun restoreFromViewModel()
    {
        //востанавливаемя из view model
        if (!mPolynomialRecyclerViewModel.isEmpty())
        {
            if (!isImageViewHolder) mPolinomRecyclerViewAdapter.setList(
                mPolynomialRecyclerViewModel.getList().clone() as ArrayList<PolynomialGroup>
            )
            else mPolinomRecyclerImageViewAdapter.setList(
                mPolynomialRecyclerViewModel.getList().clone() as ArrayList<PolynomialGroup>
            )
        }

        firstPolinom.text = SpannableStringBuilder(mPolinomEditTextViewModel.firstValue)
        secondPolinom.text = SpannableStringBuilder(mPolinomEditTextViewModel.secondValue)
    }

    private fun isRecycleViewInited() =
        (::mPolinomRecyclerView.isInitialized &&
                ::mPolinomRecyclerViewAdapter.isInitialized &&
                ::mPolinomRecyclerImageViewAdapter.isInitialized &&
                ::mPolinomRecyclerViewLayoutManager.isInitialized
                )

}



