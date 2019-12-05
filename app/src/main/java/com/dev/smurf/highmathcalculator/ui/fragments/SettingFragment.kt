package com.dev.smurf.highmathcalculator.ui.fragments

//import androidx.core.app.Fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.dev.smurf.highmathcalculator.R
import com.dev.smurf.highmathcalculator.moxyTmpAMdroisdXSupport.MvpAppCompatFragment
import com.dev.smurf.highmathcalculator.mvp.presenters.SettingsPresenter
import com.dev.smurf.highmathcalculator.mvp.views.SettingsViewInterface
import kotlinx.android.synthetic.main.fragment_setting.*



class SettingFragment : MvpAppCompatFragment() , SettingsViewInterface
{

    /*
     * состояние свитчей на момент запуска фрагмента
     */

    var matrixSwitchPreviousConsistent : Boolean = false
    var polinomSwitchPreviousConsistent : Boolean = false
    var matrixSwitchHolderMode : Boolean = false


    //вставляем презентер
    @InjectPresenter
    lateinit var mSettingsPresenter: SettingsPresenter


    private var listener: OnFragmentInteractionListener? = null




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onStart() {
        super.onStart()

        //загружаем состояние настроек
        mSettingsPresenter.update()

        //сохраняем установленный на момент запуска настройки
        matrixSwitchPreviousConsistent = swtchMatrixMode.isChecked
        polinomSwitchPreviousConsistent = swtchPolinomMode.isChecked


        swtchMatrixMode.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked)mSettingsPresenter.matrixModeSetOn()
            else mSettingsPresenter.matrixModeSetOff()
        }

        swtchPolinomMode.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked)mSettingsPresenter.polinomModeSetOn()
            else mSettingsPresenter.polinomModeSetOff()
        }

        swtchMatrixHolderMode.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked)mSettingsPresenter.matrixHolderImageModeSetOn()
            else mSettingsPresenter.matrixHolderImageModeSetOff()
        }

        btnDeleteMatrixDb.setOnClickListener {
            mSettingsPresenter.deleteMatrixDb()
        }

        btnDeletePolinomDb.setOnClickListener {
            mSettingsPresenter.deletePolinomDb()
        }
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

    override fun onPause()
    {
        super.onPause()

        if(matrixSwitchPreviousConsistent != swtchMatrixMode.isChecked && swtchMatrixMode.isChecked)
        {
            mSettingsPresenter.saveMatrixCache()
        }
        if(polinomSwitchPreviousConsistent != swtchPolinomMode.isChecked && swtchPolinomMode.isChecked)
        {
            mSettingsPresenter.savePolinomCache()
        }

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }



    override fun setMatrixModeOff()
    {
        swtchMatrixMode.isChecked = false
    }

    override fun setMatrixModeOn()
    {
        swtchMatrixMode.isChecked = true
    }

    override fun setPolinomModeOff()
    {
        swtchPolinomMode.isChecked = false
    }

    override fun setPolinomModeOn()
    {
        swtchPolinomMode.isChecked = true
    }

    override fun setMatrixHolderModeOn()
    {
        swtchMatrixHolderMode.isChecked = true
    }

    override fun setMatrixHolderModeOff()
    {
        swtchMatrixHolderMode.isChecked = false
    }

}
