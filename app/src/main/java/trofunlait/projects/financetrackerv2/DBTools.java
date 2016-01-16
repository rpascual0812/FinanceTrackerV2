package trofunlait.projects.financetrackerv2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kith on 11/30/15.
 */
public class DBTools extends SQLiteOpenHelper {
    String transactions_tbl = "transactions";
    String _id = "id";
    String _transactiontype = "transactiontype";
    String _amount = "amount";
    String _category = "category";
    String _datecreated = "datecreated";

    String categories_tbl = "categories";
    String settings_tbl = "settings";

    private ArrayList<String> categories_results = new ArrayList<String>();

    public DBTools(Context applicationContext){
        super(applicationContext, "trofunlait_financetracker.db", null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table "+transactions_tbl+" (id integer primary key autoincrement, transactiontype text, amount float, category integer, datecreated date)";
        db.execSQL(query);

        String sample = "insert into "+transactions_tbl+" (transactiontype, amount, category, datecreated) values ('Income', 100, 'Food', date('now'))";
        db.execSQL(sample);

        String categories = "create table "+categories_tbl+" (id integer primary key autoincrement, transactiontype text, category text)";
        db.execSQL(categories);

        String sample_categories = "insert into "+categories_tbl+" (transactiontype, category) values ('Income', 'Salary'), ('Income', 'Part-time Job'), ('Expense', 'Food'), ('Expense', 'Transportation');";
        db.execSQL(sample_categories);

        String settings = "create table "+settings_tbl+" (id integer primary key autoincrement, date_type text)";
        db.execSQL(settings);

        String sample_settings = "insert into "+settings_tbl+" (date_type) values ('Weekly');";
        db.execSQL(sample_settings);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "drop table if exists "+transactions_tbl;
        db.execSQL(query);
        onCreate(db);
    }

    public void insertTransactions(HashMap<String, String> queryValues){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(_transactiontype, queryValues.get(_transactiontype));
        values.put(_amount, queryValues.get(_amount));
        values.put(_category, queryValues.get(_category));
        values.put(_datecreated, queryValues.get(_datecreated));

        db.insert(transactions_tbl, null, values);

        db.close();
    }

    public void updateSettings(HashMap<String, String> queryValues){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("date_type", queryValues.get("date_type"));

        db.update(
                "settings",
                values,
                null,
                null
        );

        db.close();
    }

    public int updateTransaction(HashMap<String, String> queryValues){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        //values.put(_id, queryValues.get(_id));
        values.put(_amount, queryValues.get(_amount));
        values.put(_category, queryValues.get(_category));
        values.put(_datecreated, queryValues.get(_datecreated));

        return db.update(
                "transactions",
                values,
                "id" + " = ?",
                new String[] {queryValues.get("id")}
        );
    }

    public void insertCategory(HashMap<String, String> queryValues){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(_transactiontype, queryValues.get(_transactiontype));
        values.put(_category, queryValues.get(_category));

        db.insert(categories_tbl, null, values);

        db.close();
    }


    public List<String> getCategories(String transactiontype){
        List<String> categories = new ArrayList<String>();

        // Select All Query
        String query = "SELECT id, category FROM categories where transactiontype = '" + transactiontype + "';";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                categories.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return categories;
    }

    public List<String> getTransactions(){
        List<String> categories = new ArrayList<String>();

        // Select All Query
        String query = "SELECT id || '~' || category ||  ' - ' || amount as category FROM transactions order by datecreated desc;";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                categories.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return categories;
    }

    public List<String> getTransactionsGrouped(){
        List<String> categories = new ArrayList<String>();

        // Select All Query
        String query = "SELECT category ||  '~' || sum(amount) as trans FROM transactions group by category order by datecreated desc;";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                categories.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return categories;
    }

    public List<String> getTransactionsWeekly(){
        List<String> categories = new ArrayList<String>();

        // Select All Query
        String query = "SELECT" +
                        " category ||  '~' || sum(amount) as category " +
                        " FROM transactions " +
                        " where datecreated > date('now', 'start of day', 'weekday 1', '-7 days')" +
                        " and datecreated < date('now', 'start of day', 'weekday 6', '+1 day')" +
                        " group by category" +
                        " order by datecreated desc;";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                categories.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return categories;
    }

    public List<String> getTransactionsMonthly(){
        List<String> categories = new ArrayList<String>();

        // Select All Query
        String query = "SELECT" +
                " category ||  '~' || sum(amount) as category " +
                " FROM transactions " +
                " where datecreated > date('now', 'start of month', '-1 day')" +
                " and datecreated < date('now', 'start of month', '+1 month')" +
                " group by category" +
                " order by datecreated desc;";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                categories.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return categories;
    }

    public List<String> getTransactionsYearly(){
        List<String> categories = new ArrayList<String>();

        // Select All Query
        String query = "SELECT" +
                " category ||  '~' || sum(amount) as category " +
                " FROM transactions " +
                " where datecreated > date('now', 'start of year', '-1 day')" +
                " and datecreated < date('now', 'start of year', '+1 year')" +
                " group by category" +
                " order by datecreated desc;";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                categories.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return categories;
    }

    public List<String> getEditTransaction(String id){
        List<String> transactions = new ArrayList<String>();

        // Select All Query
        String query = "SELECT id || '~' || category ||  '~' || amount || '~' || datecreated || '~' || transactiontype FROM transactions where id = "+ id +" order by datecreated desc;";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                transactions.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return transactions;
    }

    public List<String> getAllCategories(){
        List<String> categories = new ArrayList<String>();

        // Select All Query
        String query = "SELECT id, category FROM categories;";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                categories.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return categories;
    }

    public HashMap<String, String> getSpending(String transactiontype, String setting){
        HashMap<String, String> map = new HashMap<String, String>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query;
        if(setting.equals("Weekly")){
            query = "Select " +
                    "coalesce(sum(amount),0) " +
                    "from transactions " +
                    "where transactiontype = '"+ transactiontype +"'" +
                    " and datecreated > date('now', 'start of day', 'weekday 1', '-7 days')" +
                    " and datecreated < date('now', 'start of day', 'weekday 6', '+1 day')";
        }
        else if(setting.equals("Monthly")){
            query = "Select " +
                    "coalesce(sum(amount),0) " +
                    "from transactions " +
                    "where transactiontype = '"+ transactiontype +"'" +
                    " and datecreated > date('now', 'start of month', '-1 day')" +
                    " and datecreated < date('now', 'start of month', '+1 month')";
        }
        else if(setting.equals("Yearly")){
            query = "Select " +
                    "coalesce(sum(amount),0) " +
                    "from transactions " +
                    "where transactiontype = '"+ transactiontype +"'" +
                    " and datecreated > date('now', 'start of year', '-1 day')" +
                    " and datecreated < date('now', 'start of year', '+1 year')";
        }
        else {
            query = "Select " +
                    "coalesce(sum(amount),0) " +
                    "from transactions " +
                    "where transactiontype = '"+ transactiontype +"'";
        }

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            if(transactiontype == "Income")
                map.put("Income", cursor.getString(0));
            else if(transactiontype == "Savings")
                map.put("Savings", cursor.getString(0));
            else
                map.put("Expense", cursor.getString(0));
        }

        return map;
    }

    public HashMap<String, String> getSavings(String transactiontype, String setting){
        HashMap<String, String> map = new HashMap<String, String>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query;
        if(setting.equals("Weekly")){
            query = "Select " +
                    "coalesce(sum(amount),0) " +
                    "from transactions " +
                    "where transactiontype = '"+ transactiontype +"'" +
                    " and datecreated > date('now', 'start of day', 'weekday 1', '-7 days')";
        }
        else if(setting.equals("Monthly")){
            query = "Select " +
                    "coalesce(sum(amount),0) " +
                    "from transactions " +
                    "where transactiontype = '"+ transactiontype +"'" +
                    " and datecreated < date('now', 'start of month', '-1 day')";
        }
        else if(setting.equals("Yearly")){
            query = "Select " +
                    "coalesce(sum(amount),0) " +
                    "from transactions " +
                    "where transactiontype = '"+ transactiontype +"'" +
                    " and datecreated < date('now', 'start of year', '-1 day')";
        }
        else {
            query = "Select " +
                    "coalesce(sum(amount),0) " +
                    "from transactions " +
                    "where transactiontype = '"+ transactiontype +"'";
        }

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            if(transactiontype == "Income")
                map.put("Income", cursor.getString(0));
            else if(transactiontype == "Savings")
                map.put("Savings", cursor.getString(0));
            else
                map.put("Expense", cursor.getString(0));
        }

        return map;
    }

    public HashMap<String, String> getSpendingWeekly(String transactiontype){
        HashMap<String, String> map = new HashMap<String, String>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "Select " +
                "coalesce(sum(amount),0) " +
                "from transactions " +
                "where transactiontype = '"+ transactiontype +"'" +
                " and datecreated > date('now', 'start of day', 'weekday 1', '-7 days')" +
                " and datecreated < date('now', 'start of day', 'weekday 6', '+1 day')";

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            //Log.e(">>>>>>>>>>>", cursor.getString(0));
            if(transactiontype == "Income")
                map.put("Income", cursor.getString(0));
            else if(transactiontype == "Savings")
                map.put("Savings", cursor.getString(0));
            else
                map.put("Expense", cursor.getString(0));
        }

        return map;
    }

    public HashMap<String, String> getSpendingMonthly(String transactiontype){
        HashMap<String, String> map = new HashMap<String, String>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "Select " +
                "coalesce(sum(amount),0) " +
                "from transactions " +
                "where transactiontype = '"+ transactiontype +"'" +
                " and datecreated > date('now', 'start of month', '-1 day')" +
                " and datecreated < date('now', 'start of month', '+1 month')";

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            //Log.e(">>>>>>>>>>>", cursor.getString(0));
            if(transactiontype == "Income")
                map.put("Income", cursor.getString(0));
            else if(transactiontype == "Savings")
                map.put("Savings", cursor.getString(0));
            else
                map.put("Expense", cursor.getString(0));
        }

        return map;
    }

    public HashMap<String, String> getSpendingYearly(String transactiontype){
        HashMap<String, String> map = new HashMap<String, String>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "Select " +
                        "coalesce(sum(amount),0) " +
                        "from transactions " +
                        "where transactiontype = '"+ transactiontype +"'" +
                        " and datecreated > date('now', 'start of year', '-1 day')" +
                        " and datecreated < date('now', 'start of year', '+1 year')";

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            //Log.e(">>>>>>>>>>>", cursor.getString(0));
            if(transactiontype == "Income")
                map.put("Income", cursor.getString(0));
            else if(transactiontype == "Savings")
                map.put("Savings", cursor.getString(0));
            else
                map.put("Expense", cursor.getString(0));
        }

        return map;
    }

    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }


    }

    public HashMap<String, String> getSettings(){
        HashMap<String, String> map = new HashMap<String, String>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "Select date_type from settings order by id desc";

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            map.put("date_type", cursor.getString(0));
        }

        return map;
    }
}