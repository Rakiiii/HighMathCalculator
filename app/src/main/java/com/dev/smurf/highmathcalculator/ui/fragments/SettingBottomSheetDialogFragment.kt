package com.dev.smurf.highmathcalculator.ui.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.smurf.highmathcalculator.R
import com.dev.smurf.highmathcalculator.mvp.presenters.SettingsPresenter
import com.dev.smurf.highmathcalculator.mvp.views.SettingsViewInterface
import kotlinx.android.synthetic.main.fragment_setting_bottom_sheet_dialog.*
import moxy.MvpBottomSheetDialogFragment
import moxy.presenter.InjectPresenter
import org.jetbrains.anko.support.v4.toast

class SettingBottomSheetDialogFragment : MvpBottomSheetDialogFragment(), SettingsViewInterface
{
    //вставляем презентер
    @InjectPresenter
    lateinit var mSettingsPresenter: SettingsPresenter


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting_bottom_sheet_dialog, container, false)


    }

    override fun onCancel(dialog: DialogInterface)
    {
        mSettingsPresenter.onCancelHappened()
        super.onCancel(dialog)
    }


    override fun onStart()
    {
        super.onStart()

        btnDone.setOnClickListener {
            mSettingsPresenter.doneBtnPressed()
        }

        btnDeleteMatrixDb.setOnClickListener {
            mSettingsPresenter.deleteMatrixDbBtnPressed()
        }

        btnDeletePolynomialDb.setOnClickListener {
            mSettingsPresenter.deletePolinomDbPressed()
        }

        swtchHolderMode.setOnCheckedChangeListener {
            if (swtchHolderMode.isChecked)
            {
                mSettingsPresenter.holderImageModeSetOn()
            }
            else
            {
                mSettingsPresenter.holderImageModeSetOff()
            }
        }

        swtchPolynomialMode.setOnCheckedChangeListener {
            if (swtchPolynomialMode.isChecked)
            {
                mSettingsPresenter.holderImageModeSetOn()
            }
            else
            {
                mSettingsPresenter.holderImageModeSetOff()
            }
        }

        swtchMatrixMode.setOnCheckedChangeListener {
            if (swtchMatrixMode.isChecked)
            {
                mSettingsPresenter.matrixModeSetOn()
            }
            else
            {
                mSettingsPresenter.matrixModeSetOff()
            }
        }

        //swtchHolderMode.setChecked(false)
    }

    override fun onResume()
    {
        super.onResume()
        mSettingsPresenter.settingsOpened()
    }

    override fun onDetach()
    {
        super.onDetach()
    }


    override fun setHolderImageModeOff()
    {
        swtchHolderMode.setChecked(false)
    }

    override fun setHolderImageModeOn()
    {
        swtchHolderMode.setChecked(true)
    }

    override fun setMatrixModeOff()
    {
        swtchMatrixMode.setChecked(false)
    }

    override fun setMatrixModeOn()
    {
        swtchMatrixMode.setChecked(true)
    }

    override fun setPolinomModeOff()
    {
        swtchPolynomialMode.setChecked(false)
    }

    override fun setPolinomModeOn()
    {
        swtchPolynomialMode.setChecked(true)
    }

    override fun dismissDialog()
    {
        dismiss()
    }

    override fun showToast(msg: String)
    {
        toast(msg)
    }
}
