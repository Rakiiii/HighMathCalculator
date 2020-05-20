package com.dev.smurf.highmathcalculator.ui.fragments.settingsFragment

//import androidx.core.app.Fragment

/*
class SettingFragment : MvpAppCompatFragment(), SettingsViewInterface
{

    /*
     * состояние свитчей на момент запуска фрагмента
     */

    var matrixSwitchPreviousConsistent: Boolean = false
    var polinomSwitchPreviousConsistent: Boolean = false
    var matrixSwitchHolderMode: Boolean = false


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

    override fun onStart()
    {
        super.onStart()

        //загружаем состояние настроек
        mSettingsPresenter.update()

        //сохраняем установленный на момент запуска настройки
        matrixSwitchPreviousConsistent = swtchMatrixMode.isChecked
        polinomSwitchPreviousConsistent = swtchPolinomMode.isChecked


        swtchMatrixMode.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) mSettingsPresenter.matrixModeSetOn()
            else mSettingsPresenter.matrixModeSetOff()
        }

        swtchPolinomMode.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) mSettingsPresenter.polinomModeSetOn()
            else mSettingsPresenter.polinomModeSetOff()
        }

        swtchMatrixHolderMode.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) mSettingsPresenter.holderImageModeSetOn()
            else mSettingsPresenter.holderImageModeSetOff()
        }

        btnDeleteMatrixDb.setOnClickListener {
            mSettingsPresenter.deleteMatrixDb()
        }

        btnDeletePolinomDb.setOnClickListener {
            mSettingsPresenter.deletePolinomDb()
        }

    }


    override fun onAttach(context: Context)
    {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener)
        {
            listener = context
        }
        else
        {
            //throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach()
    {
        super.onDetach()
        listener = null
    }

    override fun onPause()
    {
        super.onPause()

        if (matrixSwitchPreviousConsistent != swtchMatrixMode.isChecked && swtchMatrixMode.isChecked)
        {
            mSettingsPresenter.saveMatrixCache()
        }
        if (polinomSwitchPreviousConsistent != swtchPolinomMode.isChecked && swtchPolinomMode.isChecked)
        {
            mSettingsPresenter.savePolinomCache()
        }

    }

    interface OnFragmentInteractionListener
    {
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

    override fun setHolderImageModeOn()
    {
        swtchMatrixHolderMode.isChecked = true
    }

    override fun setHolderImageModeOff()
    {
        swtchMatrixHolderMode.isChecked = false
    }


}

*/