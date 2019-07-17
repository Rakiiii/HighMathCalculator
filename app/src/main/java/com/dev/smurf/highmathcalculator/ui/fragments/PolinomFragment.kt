package com.dev.smurf.highmathcalculator.ui.fragments

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
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
import com.dev.smurf.highmathcalculator.mvp.presenters.PolinomPresenter
import com.dev.smurf.highmathcalculator.mvp.views.PolinomViewInterface
import com.dev.smurf.highmathcalculator.ui.ViewModels.EditTextViewModel
import com.example.smurf.mtarixcalc.PolinomGroup
import com.example.smurf.mtarixcalc.PolinomRecyclerViewModel
import com.example.smurf.mtarixcalc.SwipeToDeleteCallback
import com.example.smurf.mtarixcalc.polAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_polinom.*
import org.jetbrains.anko.toast

//TODO: добавить созранение в базу данных


class PolinomFragment : MvpAppCompatFragment() , PolinomViewInterface
{

    //вставляем презентер
    @InjectPresenter
    lateinit var mPolinomPresenter: PolinomPresenter


    private var listener: OnFragmentInteractionListener? = null

    //recycler view для полиномов
    private lateinit var mPolinomRecyclerView : RecyclerView
    private lateinit var mPolinomRecyclerViewAdapter : polAdapter
    private lateinit var mPolinomRecyclerViewLayoutManager: LinearLayoutManager

    private lateinit var mPolinomRecyclerViewModel: PolinomRecyclerViewModel

    private lateinit var mPolinomEditTextViewModel : EditTextViewModel

    private var isLoaded : Boolean = false


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
        mPolinomRecyclerViewModel = ViewModelProviders.of(activity as FragmentActivity).get(PolinomRecyclerViewModel::class.java)

        //инициализация view model для содержимого edittext
        mPolinomEditTextViewModel = ViewModelProviders.of(activity as FragmentActivity).get(EditTextViewModel::class.java)

        //инициализация recyclerView для полиномов
        initRecyclerView()

        //добавление удаления свайпом
        enableSwipeToDeleteAndUndo()

        //востанавливаемя из view model
        if(!mPolinomRecyclerViewModel.isEmpty())mPolinomRecyclerViewAdapter.setList(mPolinomRecyclerViewModel.getList())

        if(!isLoaded)
        {
            mPolinomPresenter.onLoadSavedInstance()
            isLoaded = true
        }

        firstPolinom.text = SpannableStringBuilder(mPolinomEditTextViewModel.firstValue)
        secondPolinom.text = SpannableStringBuilder(mPolinomEditTextViewModel.secondValue)



        btnPolPlus.setOnClickListener {
            v -> mPolinomPresenter.onPlusClick(firstPolinom.text.toString() , secondPolinom.text.toString())
        }

        btnPolMinus.setOnClickListener {
            v -> mPolinomPresenter.onMinusClick(firstPolinom.text.toString() , secondPolinom.text.toString())
        }

        btnPolTimes.setOnClickListener {
            v -> mPolinomPresenter.onTimesClick(firstPolinom.text.toString() , secondPolinom.text.toString())
        }

        btnPolDiv.setOnClickListener {
            v -> mPolinomPresenter.onDivisionClick(firstPolinom.text.toString() , secondPolinom.text.toString())
        }

        btnPolRootsA.setOnClickListener {
            v -> mPolinomPresenter.onRootsOfClick(firstPolinom.text.toString())
        }

        btnPolRootsB.setOnClickListener {
            v -> mPolinomPresenter.onRootsOfClick(secondPolinom.text.toString())
        }

        btnSwap.setOnClickListener {
            val tmp = firstPolinom.text
            firstPolinom.text = secondPolinom.text
            secondPolinom.text = tmp
        }


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
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
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
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    fun initRecyclerView()
    {
        mPolinomRecyclerViewLayoutManager = LinearLayoutManager(this.context)
        mPolinomRecyclerViewAdapter = polAdapter( this.context!! , firstPolinom , secondPolinom)
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


                var isUnded = false

                val position = viewHolder.adapterPosition
                val item = mPolinomRecyclerViewAdapter.getData(position)

                mPolinomRecyclerViewAdapter.removeElement(position)


                val snackbar = Snackbar
                    .make( polinomFrame , "Item was removed from the list.", Snackbar.LENGTH_LONG)
                snackbar.setAction("UNDO")
                {
                    mPolinomRecyclerViewAdapter.restoreItem(item, position)
                    isUnded = true
                    mPolinomRecyclerView.scrollToPosition(position)
                }

                snackbar.setActionTextColor(Color.YELLOW)
                snackbar.show()

                //mPolinomRecyclerViewModel.updateList(mPolinomRecyclerViewAdapter.getList().clone() as ArrayList<PolinomGroup>)
                if(!isUnded)
                {
                    mPolinomPresenter.deleteFromDb(item)
                    mPolinomRecyclerViewModel.deleteItem(item)
                }
            }

        }

        val itemTouchhelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchhelper.attachToRecyclerView(mPolinomRecyclerView)
    }


    override fun addToPolinomRecyclerView(obj: PolinomGroup)
    {
        mPolinomRecyclerViewAdapter.addElement(mPolinomRecyclerViewModel.add(obj))
    }

    override fun showToast(obj: String)
    {
        this.context!!.toast(obj)
    }

    override fun setRecyclerViewList(ar: ArrayList<PolinomGroup>)
    {
        mPolinomRecyclerViewModel.updateList(ar.clone() as ArrayList<PolinomGroup>)
        mPolinomRecyclerViewAdapter.setList(ar)
    }
}
