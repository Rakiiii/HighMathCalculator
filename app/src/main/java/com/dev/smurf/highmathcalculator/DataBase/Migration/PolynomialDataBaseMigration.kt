package com.dev.smurf.highmathcalculator.DataBase.Migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class PolynomialDataBaseMigration : Migration(1,2)
{
    override fun migrate(database: SupportSQLiteDatabase)
    {
        database.execSQL("ALTER TABLE PolynomialGroup DROP COLUMN isSolved")
    }
}