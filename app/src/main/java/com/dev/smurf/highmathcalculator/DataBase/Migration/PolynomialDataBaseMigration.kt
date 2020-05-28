package com.dev.smurf.highmathcalculator.DataBase.Migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dev.smurf.highmathcalculator.Polynomials.PolynomialBase

object PolynomialDataBaseMigration_2_3 : Migration(2,3)
{
    override fun migrate(database: SupportSQLiteDatabase)
    {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS Tmp (polLeftPolynomial TEXT NOT NULL, polOstPolynomial TEXT NOT NULL, polResPolynomial TEXT NOT NULL,polSignPolynomial TEXT NOT NULL,roots TEXT,polRightPolynomial TEXT NOT NULL, time TEXT PRIMARY KEY  NOT NULL)"
        )

        database.execSQL(
            "INSERT INTO Tmp (polLeftPolynomial,polOstPolynomial,polResPolynomial,polSignPolynomial,roots,polRightPolynomial,time) SELECT polLeftPolynomial,polOstPolynomial,polResPolynomial,polSignPolynomial,roots,polRightPolynomial,time FROM PolynomialGroup"
        )

        database.execSQL(
            "DROP TABLE PolynomialGroup"
        )

        database.execSQL("ALTER TABLE Tmp RENAME TO PolynomialGroup")
    }
}

object PolynomialDataBaseMigration_3_4 : Migration(3,4)
{
    override fun migrate(database: SupportSQLiteDatabase)
    {
        val str = "\""+PolynomialBase.EmptyPolynomial.supString().substringBefore('@')+"\""
        val change = "\"\""
        database.execSQL(
            "UPDATE PolynomialGroup Set polRightPolynomial = Replace(polRightPolynomial, $str , $change)"
        )
        database.execSQL(
            "UPDATE PolynomialGroup Set polOstPolynomial = Replace(polOstPolynomial, $str , $change)"
        )

        database.execSQL(
            "UPDATE PolynomialGroup Set polResPolynomial = Replace(polResPolynomial, $str , $change)"
        )
    }
}

object PolynomialDataBaseMigration_4_5 : Migration(4,5)
{
    override fun migrate(database: SupportSQLiteDatabase)
    {
        val str = "\"%@%\""
        val change = "\"\""
        database.execSQL(
            "UPDATE PolynomialGroup Set polRightPolynomial = case when polRightPolynomial LIKE($str) then $change else polRightPolynomial end"
        )
        database.execSQL(
            "UPDATE PolynomialGroup Set polOstPolynomial = case when polOstPolynomial LIKE($str)  then $change else polOstPolynomial end"
        )

        database.execSQL(
            "UPDATE PolynomialGroup Set polResPolynomial = case when polResPolynomial LIKE($str) then $change else polResPolynomial end"
        )
    }
}