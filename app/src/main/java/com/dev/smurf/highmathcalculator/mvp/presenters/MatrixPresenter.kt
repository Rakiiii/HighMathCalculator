package com.dev.smurf.highmathcalculator.mvp.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.dev.smurf.highmathcalculator.mvp.models.MatrixModel
import com.dev.smurf.highmathcalculator.mvp.views.MatrixViewInterface

@InjectViewState
class MatrixPresenter : MvpPresenter<MatrixViewInterface>()
{
    private var mMatrixModel = MatrixModel()

    fun onPlusClick(firstMatrix : String , secondMatrix : String)
    {
        viewState.addToRecyclerView(mMatrixModel.plus(mMatrixModel.createMatrix(firstMatrix) , mMatrixModel.createMatrix(secondMatrix)))
    }

    fun onMinusClick(firstMatrix : String , secondMatrix : String)
    {
        viewState.addToRecyclerView(mMatrixModel.minus(mMatrixModel.createMatrix(firstMatrix) , mMatrixModel.createMatrix(secondMatrix)))
    }

    fun onTimesClick(firstMatrix : String , secondMatrix : String)
    {
        viewState.addToRecyclerView(mMatrixModel.times(mMatrixModel.createMatrix(firstMatrix) , mMatrixModel.createMatrix(secondMatrix)))
    }

    fun onInversClick(firstMatrix: String)
    {
        viewState.addToRecyclerView(mMatrixModel.invers(mMatrixModel.createMatrix(firstMatrix)))
    }

    fun onDeterminantClick(firstMatrix: String)
    {
        viewState.addToRecyclerView(mMatrixModel.determinant(mMatrixModel.createMatrix(firstMatrix)))
    }

}