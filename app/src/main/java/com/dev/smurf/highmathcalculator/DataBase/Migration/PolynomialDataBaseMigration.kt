package com.dev.smurf.highmathcalculator.DataBase.Migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

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