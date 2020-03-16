package com.dev.smurf.highmathcalculator.ui.fragments

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
import com.arellomobile.mvp.presenter.InjectPresenter
import com.dev.smurf.highmathcalculator.R
import com.dev.smurf.highmathcalculator.moxyTmpAMdroisdXSupport.MvpAppCompatFragment
import com.dev.smurf.highmathcalculator.mvp.presenters.PolynomialPresenter
import com.dev.smurf.highmathcalculator.mvp.views.PolynomialViewInterface
import com.dev.smurf.highmathcalculator.ui.ViewModels.EditTextViewModel
import com.dev.smurf.highmathcalculator.ui.adapters.PolynomialAdapterImageView
import com.example.smurf.mtarixcalc.PolynomialGroup
import com.example.smurf.mtarixcalc.PolynomialRecyclerViewModel
import com.example.smurf.mtarixcalc.PolynomialTxtAdapter
import com.example.smurf.mtarixcalc.SwipeToDeleteCallback
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_polinom.*
import org.jetbrains.anko.toast

class PolynomialFragment : MvpAppCompatFragment(), PolynomialViewInterface
{

    //buffer for toast, because amount off ui threads is finite
    val Toasts = MutableList(0) { "" }
    val toastHandler = Handler()

    //вставляем презентер
    @InjectPresenter
    lateinit var mPolynomialPresenter: PolynomialPresenter


    private var listener: OnFragmentInteractionListener? = null

    //recycler view для полиномов
    private lateinit var mPolinomRecyclerView: RecyclerView
    private lateinit var mPolinomRecyclerViewAdapter: PolynomialTxtAdapter
    private lateinit var mPolinomRecyclerImageViewAdapter: PolynomialAdapterImageView
    private lateinit var mPolinomRecyclerViewLayoutManager: LinearLayoutManager

    private lateinit var mPolynomialRecyclerViewModel: PolynomialRecyclerViewModel

    private lateinit var mPolinomEditTextViewModel: EditTextViewModel

    private var isLoaded: Boolean = false
    private var isImageViewHolder = false


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

        //инициализация view model для содержимого edittext
        mPolinomEditTextViewModel =
            ViewModelProviders.of(activity as FragmentActivity).get(EditTextViewModel::class.java)

        //инициализация recyclerView для полиномов
        initRecyclerView()

        //добавление удаления свайпом
        enableSwipeToDeleteAndUndo()


        //добовляем обработчики
        btnPolPlus.setOnClickListener { v ->
            mPolynomialPresenter.onPlusClick(firstPolinom.text.toString(), secondPolinom.text.toString())
        }

        btnPolMinus.setOnClickListener { v ->
            mPolynomialPresenter.onMinusClick(firstPolinom.text.toString(), secondPolinom.text.toString())
        }

        btnPolTimes.setOnClickListener { v ->
            mPolynomialPresenter.onTimesClick(firstPolinom.text.toString(), secondPolinom.text.toString())
        }

        btnPolDiv.setOnClickListener { v ->
            mPolynomialPresenter.onDivisionClick(firstPolinom.text.toString(), secondPolinom.text.toString())
        }

        btnPolRootsA.setOnClickListener { v ->
            mPolynomialPresenter.onRootsOfClick(firstPolinom.text.toString())
        }

        btnPolRootsB.setOnClickListener { v ->
            mPolynomialPresenter.onRootsOfClick(secondPolinom.text.toString())
        }

        btnSwapPolynomial.setOnClickListener {
            val tmp = firstPolinom.text
            firstPolinom.text = secondPolinom.text
            secondPolinom.text = tmp
        }

        //востанавливаемя из view model
        if (!mPolynomialRecyclerViewModel.isEmpty()) mPolinomRecyclerViewAdapter.setList(mPolynomialRecyclerViewModel.getList())

        firstPolinom.text = SpannableStringBuilder(mPolinomEditTextViewModel.firstValue)
        secondPolinom.text = SpannableStringBuilder(mPolinomEditTextViewModel.secondValue)

        if (!isLoaded)
        {
            mPolynomialPresenter.onLoadSavedInstance()
            isLoaded = true
        }


        if (mPolynomialPresenter.checkImageMode())
        {
            setImageAdapter()
            isImageViewHolder = true
        }

        checkToast()
    }

    override fun onStop()
    {
        mPolinomEditTextViewModel.firstValue = firstPolinom.text.toString()
        mPolinomEditTextViewModel.secondValue = secondPolinom.text.toString()


        super.onStop()
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


    interface OnFragmentInteractionListener
    {
        fun onFragmentInteraction(uri: Uri)
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
        mPolinomRecyclerImageViewAdapter.setList(mPolinomRecyclerViewAdapter.getList().clone() as ArrayList<PolynomialGroup>)
        mPolinomRecyclerView.swapAdapter(mPolinomRecyclerImageViewAdapter, true)
        mPolinomRecyclerImageViewAdapter.notifyDataSetChanged()
    }

    fun setTxtAdapter()
    {
        mPolinomRecyclerViewAdapter.setList(mPolinomRecyclerImageViewAdapter.getList().clone() as ArrayList<PolynomialGroup>)
        mPolinomRecyclerView.swapAdapter(mPolinomRecyclerViewAdapter, true)
        mPolinomRecyclerViewAdapter.notifyDataSetChanged()
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

}



