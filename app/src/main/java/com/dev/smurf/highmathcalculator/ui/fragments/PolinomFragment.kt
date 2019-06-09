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
import com.dev.smurf.highmathcalculator.mvp.presenters.PolinomPresenter
import com.dev.smurf.highmathcalculator.mvp.views.PolinomViewInterface
import com.example.smurf.mtarixcalc.PolinomRecyclerViewModel
import com.example.smurf.mtarixcalc.SwipeToDeleteCallback
import com.example.smurf.mtarixcalc.polAdapter
import com.example.smurf.mtarixcalc.polGroup
import kotlinx.android.synthetic.main.fragment_polinom.*


class PolinomFragment : MvpFragment() , PolinomViewInterface
{

    @InjectPresenter
    lateinit var mPolinomPresenter: PolinomPresenter


    private var listener: OnFragmentInteractionListener? = null

    //recycler view для полиномов\
    private lateinit var polRecycler : RecyclerView
    private lateinit var polAdapter: polAdapter
    private lateinit var polLayoutManager: LinearLayoutManager

    private lateinit var mPolinomRecyclerViewModel: PolinomRecyclerViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_polinom, container, false)
    }

    override fun onStart() {
        super.onStart()
        //инициализация viewModel для полиномов
        mPolinomRecyclerViewModel = ViewModelProviders.of(activity as FragmentActivity).get(PolinomRecyclerViewModel::class.java)

        //инициализация recyclerView для полиномов
        initRecyclerView()

        //добавление удаления свайпом
        enableSwipeToDeleteAndUndo()

        //востанавливаемя из view model
        if(!mPolinomRecyclerViewModel.isEmpty())polAdapter.setList(mPolinomRecyclerViewModel.getList())
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            //throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    fun initRecyclerView()
    {
        polLayoutManager = LinearLayoutManager(this.context)
        polAdapter = polAdapter( this.context , firstPolinom , secondPolinom)
        polRecycler = view.findViewById(R.id.polinomRecycler)
        polRecycler.adapter = polAdapter
        polRecycler.layoutManager = polLayoutManager
    }

    private fun enableSwipeToDeleteAndUndo()
    {
        val swipeToDeleteCallback = object : SwipeToDeleteCallback(this.context) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {


                val position = viewHolder.adapterPosition
                val item = polAdapter.getData(position)

                polAdapter.removeElement(position)


                val snackbar = Snackbar
                    .make( polinomFrame , "Item was removed from the list.", Snackbar.LENGTH_LONG)
                snackbar.setAction("UNDO") {
                    polAdapter.restoreItem(item, position)
                    polRecycler.scrollToPosition(position)
                }

                snackbar.setActionTextColor(Color.YELLOW)
                snackbar.show()

                //mPolinomRecyclerViewModel.updateList( polAdapter.getList())

            }
        }

        val itemTouchhelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchhelper.attachToRecyclerView(polRecycler)
    }


    override fun addToPolinomRecyclerView(obj: polGroup)
    {
        polAdapter.addElement(mPolinomRecyclerViewModel.add(obj))
    }
}
