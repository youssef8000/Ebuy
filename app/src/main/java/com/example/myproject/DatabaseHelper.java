package com.example.myproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper{
    public DatabaseHelper(@Nullable Context context) {
        super(context, "ebuyshopping.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase MyDatabase) {
        try {
            MyDatabase.execSQL("CREATE TABLE users(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "email TEXT UNIQUE," +
                    "password TEXT," +
                    "date TEXT)");

            MyDatabase.execSQL("CREATE TABLE category(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT UNIQUE," +
                    "image BLOB)");

            MyDatabase.execSQL("CREATE TABLE product(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "description TEXT," +
                    "image BLOB," +
                    "barcode TEXT," +
                    "category INTEGER," +
                    "price INTEGER," +
                    "quantity INTEGER," +
                    "FOREIGN KEY(category) REFERENCES category(id))");

            MyDatabase.execSQL("CREATE TABLE shoppingcart(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "userId INTEGER," +
                    "proId INTEGER," +
                    "quantity INTEGER," +
                    "totalprice INTEGER," +
                    "FOREIGN KEY(userId) REFERENCES users(id),"+
                    "FOREIGN KEY(proId) REFERENCES product(id))");

            MyDatabase.execSQL("CREATE TABLE userorder(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "userId INTEGER," +
                    "proId INTEGER," +
                    "quantity INTEGER," +
                    "totalprice INTEGER," +
                    "feedback TEXT," +
                    "rating INTEGER," +
                    "FOREIGN KEY(userId) REFERENCES users(id),"+
                    "FOREIGN KEY(proId) REFERENCES product(id))");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        try {
            MyDB.execSQL("DROP TABLE IF EXISTS users");
            MyDB.execSQL("DROP TABLE IF EXISTS category");
            MyDB.execSQL("DROP TABLE IF EXISTS product");
            MyDB.execSQL("DROP TABLE IF EXISTS shoppingcart");
            MyDB.execSQL("DROP TABLE IF EXISTS userorder");
            onCreate(MyDB);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Boolean insertData(String email, String password,String birthdate){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        contentValues.put("date", birthdate);
        long result = MyDatabase.insert("users", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public User getUserByEmail(String email) {
        SQLiteDatabase MyDatabase = this.getReadableDatabase();
        Cursor cursor = MyDatabase.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});
        User user = null;
        try {
            if (cursor != null && cursor.moveToFirst()) {
                int idColumnIndex = cursor.getColumnIndex("id");
                int emailColumnIndex = cursor.getColumnIndex("email");
                int userId = cursor.getInt(idColumnIndex);
                String userEmail = cursor.getString(emailColumnIndex);
                user = new User(userId, userEmail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return user;
    }
    public User getUserById(int userId) {
        SQLiteDatabase myDatabase = this.getReadableDatabase();
        Cursor cursor = myDatabase.rawQuery("SELECT * FROM users WHERE id = ?", new String[]{String.valueOf(userId)});
        User user = null;

        try {
            if (cursor != null && cursor.moveToFirst()) {
                int idColumnIndex = cursor.getColumnIndex("id");
                int emailColumnIndex = cursor.getColumnIndex("email");

                int retrievedUserId = cursor.getInt(idColumnIndex);
                String userEmail = cursor.getString(emailColumnIndex);

                user = new User(retrievedUserId, userEmail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return user;
    }
    public Boolean updatePassword(String email, String password){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", password);
        long result = MyDatabase.update("users", contentValues, "email=?",new String[]{email});
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Boolean checkEmail(String email){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from users where email = ?", new String[]{email});
        if(cursor.getCount() > 0) {
            return true;
        }else {
            return false;
        }
    }
    public Boolean checkEmailPassword(String email, String password){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from users where email = ? and password = ?", new String[]{email, password});
        if (cursor.getCount() > 0) {
            return true;
        }else {
            return false;
        }
    }
    public Boolean insertcategory(String name,byte[]image){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("image", image);
        long result = MyDatabase.insert("category", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Boolean updateCategory(int id, String name,byte[]image) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("image", image);
        long result = MyDatabase.update("category", contentValues, "id=?", new String[]{String.valueOf(id)});
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    // Modify this method to retrieve categories with images
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        SQLiteDatabase myDatabase = this.getReadableDatabase();
        Cursor cursor = myDatabase.rawQuery("SELECT id, name, image FROM category", null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                int idColumnIndex = cursor.getColumnIndex("id");
                int nameColumnIndex = cursor.getColumnIndex("name");
                int imageColumnIndex = cursor.getColumnIndex("image");

                do {
                    int categoryId = cursor.getInt(idColumnIndex);
                    String categoryName = cursor.getString(nameColumnIndex);
                    byte[] categoryImage = cursor.getBlob(imageColumnIndex);
                    categories.add(new Category(categoryId, categoryName, categoryImage));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return categories;
    }
    public Category getOnecat(int catId) {
        SQLiteDatabase myDatabase = this.getReadableDatabase();
        Cursor cursor = myDatabase.rawQuery("SELECT * FROM category WHERE id = ?", new String[]{String.valueOf(catId)});
        Category category = null;
        try {
            if (cursor != null && cursor.moveToFirst()) {
                int idColumnIndex = cursor.getColumnIndex("id");
                int nameColumnIndex = cursor.getColumnIndex("name");
                int imageColumnIndex = cursor.getColumnIndex("image");

                int fetchedProductId = cursor.getInt(idColumnIndex);
                String productName = cursor.getString(nameColumnIndex);
                byte[] imageName = cursor.getBlob(imageColumnIndex);
                category = new Category(fetchedProductId, productName,imageName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return category;
    }
    public boolean deleteCategoryById(int categoryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("category", "id=?", new String[]{String.valueOf(categoryId)}) > 0;
    }
    public Boolean insertProduct(String name, String description,byte[] image,String barcode,int catId, String price, String quantity) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("description", description);
        contentValues.put("image", image);
        contentValues.put("barcode", barcode);
        contentValues.put("category", catId);
        contentValues.put("price", price);
        contentValues.put("quantity", quantity);

        long result = MyDatabase.insert("product", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean UpdateProduct(int productId, String name, String description, byte[] image, String barcode, int catId, int price, int quantity) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("description", description);
        contentValues.put("image", image);
        contentValues.put("barcode", barcode);
        contentValues.put("category", catId);
        contentValues.put("price", price);
        contentValues.put("quantity", quantity);
        long result = MyDatabase.update("product", contentValues, "id=?", new String[]{String.valueOf(productId)});

        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }
    public boolean updateProductQuantity(int productId, int quantity) {
        SQLiteDatabase myDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("quantity", quantity);
        long result = myDatabase.update("product", contentValues, "id=?", new String[]{String.valueOf(productId)});
        myDatabase.close();
        return result != -1;
    }
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase myDatabase = this.getReadableDatabase();
        Cursor cursor = myDatabase.rawQuery("SELECT * FROM product", null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                int idColumnIndex = cursor.getColumnIndex("id");
                int nameColumnIndex = cursor.getColumnIndex("name");
                int descriptionColumnIndex = cursor.getColumnIndex("description");
                int imageColumnIndex = cursor.getColumnIndex("image");
                int barColumnIndex = cursor.getColumnIndex("barcode");
                int categoryColumnIndex = cursor.getColumnIndex("category");
                int priceColumnIndex = cursor.getColumnIndex("price");
                int quantityColumnIndex = cursor.getColumnIndex("quantity");
                do {
                    int productId = cursor.getInt(idColumnIndex);
                    String productName = cursor.getString(nameColumnIndex);
                    String productDescription = cursor.getString(descriptionColumnIndex);
                    byte[] productImage = cursor.getBlob(imageColumnIndex);
                    String productbarcode = cursor.getString(barColumnIndex);
                    int productCategory = cursor.getInt(categoryColumnIndex);
                    int productPrice = cursor.getInt(priceColumnIndex);
                    String productQuantity = cursor.getString(quantityColumnIndex);
                    Product product = new Product(productId, productName, productDescription, productImage,productbarcode ,productCategory, productPrice, productQuantity);
                    products.add(product);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return products;
    }

    public Product getProductByBarcode(String barcode) {
        SQLiteDatabase myDatabase = this.getReadableDatabase();
        Cursor cursor = myDatabase.rawQuery("SELECT * FROM product WHERE barcode = ?", new String[]{barcode});

        Product product = null;

        try {
            if (cursor != null && cursor.moveToFirst()) {
                int idColumnIndex = cursor.getColumnIndex("id");
                int nameColumnIndex = cursor.getColumnIndex("name");
                int descriptionColumnIndex = cursor.getColumnIndex("description");
                int imageColumnIndex = cursor.getColumnIndex("image");
                int categoryColumnIndex = cursor.getColumnIndex("category");
                int priceColumnIndex = cursor.getColumnIndex("price");
                int quantityColumnIndex = cursor.getColumnIndex("quantity");

                int productId = cursor.getInt(idColumnIndex);
                String productName = cursor.getString(nameColumnIndex);
                String productDescription = cursor.getString(descriptionColumnIndex);
                byte[] productImage = cursor.getBlob(imageColumnIndex);
                int categoryId = cursor.getInt(categoryColumnIndex);
                int productPrice = cursor.getInt(priceColumnIndex);
                String productQuantity = cursor.getString(quantityColumnIndex);

                product = new Product(productId, productName, productDescription, productImage, barcode, categoryId, productPrice, productQuantity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return product;
    }
    public List<Product> getAllCATProducts(int categoryId) {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase myDatabase = this.getReadableDatabase();
        Cursor cursor = myDatabase.rawQuery("SELECT * FROM product WHERE category = ?", new String[]{String.valueOf(categoryId)});
        try {
            if (cursor != null && cursor.moveToFirst()) {
                int idColumnIndex = cursor.getColumnIndex("id");
                int nameColumnIndex = cursor.getColumnIndex("name");
                int descriptionColumnIndex = cursor.getColumnIndex("description");
                int imageColumnIndex = cursor.getColumnIndex("image");
                int barColumnIndex = cursor.getColumnIndex("barcode");
                int categoryColumnIndex = cursor.getColumnIndex("category");
                int priceColumnIndex = cursor.getColumnIndex("price");
                int quantityColumnIndex = cursor.getColumnIndex("quantity");
                do {
                    int productIdFromCursor = cursor.getInt(idColumnIndex);
                    String productName = cursor.getString(nameColumnIndex);
                    String productDescription = cursor.getString(descriptionColumnIndex);
                    byte[] productImage = cursor.getBlob(imageColumnIndex);
                    String productbarcode = cursor.getString(barColumnIndex);
                    int productCategory = cursor.getInt(categoryColumnIndex);
                    int productPrice = cursor.getInt(priceColumnIndex);
                    String productQuantity = cursor.getString(quantityColumnIndex);
                    Product product = new Product(productIdFromCursor, productName, productDescription, productImage,productbarcode, productCategory, productPrice, productQuantity);
                    products.add(product);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return products;
    }
    public List<Product> searchProducts(String searchText) {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase myDatabase = this.getReadableDatabase();

        String[] projection = {"id", "name", "description", "image", "barcode", "category", "price", "quantity"};
        String selection = null;
        String[] selectionArgs = null;

        // Check if search text is provided
        if (searchText != null && !searchText.isEmpty()) {
            selection = "name LIKE ? OR description LIKE ?";
            selectionArgs = new String[]{"%" + searchText + "%", "%" + searchText + "%"};
        }

        Cursor cursor = myDatabase.query("product", projection, selection, selectionArgs, null, null, null);

        try {
            while (cursor != null && cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndex("id");
                int nameIndex = cursor.getColumnIndex("name");
                int descriptionIndex = cursor.getColumnIndex("description");
                int imageIndex = cursor.getColumnIndex("image");
                int barIndex = cursor.getColumnIndex("barcode");
                int categoryIndex = cursor.getColumnIndex("category");
                int priceIndex = cursor.getColumnIndex("price");
                int quantityIndex = cursor.getColumnIndex("quantity");

                if (idIndex != -1 && nameIndex != -1 && descriptionIndex != -1 &&
                        imageIndex != -1 && barIndex != -1 && categoryIndex != -1 && priceIndex != -1 && quantityIndex != -1) {
                    int id = cursor.getInt(idIndex);
                    String name = cursor.getString(nameIndex);
                    String description = cursor.getString(descriptionIndex);
                    byte[] image = cursor.getBlob(imageIndex);
                    String barcode = cursor.getString(barIndex);
                    int category = cursor.getInt(categoryIndex);
                    int price = cursor.getInt(priceIndex);
                    String quantity = cursor.getString(quantityIndex);

                    Product product = new Product(id, name, description, image, barcode, category, price, quantity);
                    productList.add(product);
                } else {
                    Log.w("SearchProducts", "One or more columns not found in the result set");
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return productList;
    }
    public Product getOneProduct(int productId) {
        SQLiteDatabase myDatabase = this.getReadableDatabase();
        Cursor cursor = myDatabase.rawQuery("SELECT * FROM product WHERE id = ?", new String[]{String.valueOf(productId)});
        Product product = null;
        try {
            if (cursor != null && cursor.moveToFirst()) {
                int idColumnIndex = cursor.getColumnIndex("id");
                int nameColumnIndex = cursor.getColumnIndex("name");
                int descriptionColumnIndex = cursor.getColumnIndex("description");
                int imageColumnIndex = cursor.getColumnIndex("image");
                int barColumnIndex = cursor.getColumnIndex("barcode");
                int categoryColumnIndex = cursor.getColumnIndex("category");
                int priceColumnIndex = cursor.getColumnIndex("price");
                int quantityColumnIndex = cursor.getColumnIndex("quantity");

                int fetchedProductId = cursor.getInt(idColumnIndex);
                String productName = cursor.getString(nameColumnIndex);
                String productDescription = cursor.getString(descriptionColumnIndex);
                byte[] productImage = cursor.getBlob(imageColumnIndex);
                String productbarcode = cursor.getString(barColumnIndex);
                int productCategory = cursor.getInt(categoryColumnIndex);
                int productPrice = cursor.getInt(priceColumnIndex);
                String productQuantity = cursor.getString(quantityColumnIndex);
                product = new Product(fetchedProductId, productName, productDescription, productImage,productbarcode ,productCategory, productPrice, productQuantity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return product;
    }
    public Category getOnecatProduct(int productcatId) {
        SQLiteDatabase myDatabase = this.getReadableDatabase();
        Cursor cursor = myDatabase.rawQuery("SELECT * FROM category WHERE id = ?", new String[]{String.valueOf(productcatId)});
        Category category = null;
        try {
            if (cursor != null && cursor.moveToFirst()) {
                int idColumnIndex = cursor.getColumnIndex("id");
                int nameColumnIndex = cursor.getColumnIndex("name");
                int imageColumnIndex = cursor.getColumnIndex("image");

                int fetchedProductId = cursor.getInt(idColumnIndex);
                String productName = cursor.getString(nameColumnIndex);
                byte[] productimage = cursor.getBlob(imageColumnIndex);

                category = new Category(fetchedProductId, productName,productimage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return category;
    }
    public boolean deleteproductById(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("product", "id=?", new String[]{String.valueOf(productId)}) > 0;
    }

    public boolean insertShoppingCartItem(int userId, int proId, int quantity, int totalprice) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userId", userId);
        contentValues.put("proId", proId);
        contentValues.put("quantity", quantity);
        contentValues.put("totalprice", totalprice);

        long result = MyDatabase.insert("shoppingcart", null, contentValues);
        return result != -1;
    }
    public List<ShoppingCartItem> getAllShoppingCartItems(int userId) {
        List<ShoppingCartItem> shoppingCartItems = new ArrayList<>();
        SQLiteDatabase myDatabase = this.getReadableDatabase();
        Cursor cursor = myDatabase.rawQuery("SELECT * FROM shoppingcart WHERE userId = ?", new String[]{String.valueOf(userId)});
        try {
            if (cursor != null && cursor.moveToFirst()) {
                int idColumnIndex = cursor.getColumnIndex("id");
                int proIdColumnIndex = cursor.getColumnIndex("proId");
                int quantityColumnIndex = cursor.getColumnIndex("quantity");
                int totalpriceColumnIndex = cursor.getColumnIndex("totalprice");

                do {
                    int itemId = cursor.getInt(idColumnIndex);
                    int productId = cursor.getInt(proIdColumnIndex);
                    int itemQuantity = cursor.getInt(quantityColumnIndex);
                    int itemTotalPrice = cursor.getInt(totalpriceColumnIndex);

                    ShoppingCartItem item = new ShoppingCartItem(itemId, userId, productId, itemQuantity, itemTotalPrice);
                    shoppingCartItems.add(item);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return shoppingCartItems;
    }
    public boolean updateShoppingCartItem(int itemId, int quantity, int totalprice) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("quantity", quantity);
        contentValues.put("totalprice", totalprice);
        long result = MyDatabase.update("shoppingcart", contentValues, "id=?", new String[]{String.valueOf(itemId)});
        return result != -1;
    }
    public boolean deleteShoppingCartItem(int itemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("shoppingcart", "id=?", new String[]{String.valueOf(itemId)}) > 0;
    }
    public boolean insertUserOrder(int userId, int proId, int quantity, int totalprice, String feedback, int rating) {
        SQLiteDatabase myDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userId", userId);
        contentValues.put("proId", proId);
        contentValues.put("quantity", quantity);
        contentValues.put("totalprice", totalprice);
        contentValues.put("feedback", feedback);
        contentValues.put("rating", rating);

        long result = myDatabase.insert("userorder", null, contentValues);
        return result != -1;
    }

    public List<UserOrder> getAllUserOrders(int userId) {
        List<UserOrder> userOrders = new ArrayList<>();
        SQLiteDatabase myDatabase = this.getReadableDatabase();
        Cursor cursor = myDatabase.rawQuery("SELECT * FROM userorder WHERE userId = ?", new String[]{String.valueOf(userId)});

        try {
            if (cursor != null && cursor.moveToFirst()) {
                int idColumnIndex = cursor.getColumnIndex("id");
                int proIdColumnIndex = cursor.getColumnIndex("proId");
                int quantityColumnIndex = cursor.getColumnIndex("quantity");
                int totalpriceColumnIndex = cursor.getColumnIndex("totalprice");
                int feedbackColumnIndex = cursor.getColumnIndex("feedback");
                int ratingColumnIndex = cursor.getColumnIndex("rating");
                do {
                    int orderId = cursor.getInt(idColumnIndex);
                    int productId = cursor.getInt(proIdColumnIndex);
                    int orderQuantity = cursor.getInt(quantityColumnIndex);
                    int orderTotalPrice = cursor.getInt(totalpriceColumnIndex);
                    String orderFeedback = cursor.getString(feedbackColumnIndex);
                    int orderRating = cursor.getInt(ratingColumnIndex);

                    UserOrder userOrder = new UserOrder(orderId, userId, productId, orderQuantity, orderTotalPrice, orderFeedback, orderRating);
                    userOrders.add(userOrder);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return userOrders;
    }
    public List<UserOrder> getAllOrders() {
        List<UserOrder> userOrders = new ArrayList<>();
        SQLiteDatabase myDatabase = this.getReadableDatabase();
        Cursor cursor = myDatabase.rawQuery("SELECT * FROM userorder", null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                int idColumnIndex = cursor.getColumnIndex("id");
                int userIdColumnIndex = cursor.getColumnIndex("userId");
                int proIdColumnIndex = cursor.getColumnIndex("proId");
                int quantityColumnIndex = cursor.getColumnIndex("quantity");
                int totalpriceColumnIndex = cursor.getColumnIndex("totalprice");
                int feedbackColumnIndex = cursor.getColumnIndex("feedback");
                int ratingColumnIndex = cursor.getColumnIndex("rating");

                do {
                    int orderId = cursor.getInt(idColumnIndex);
                    int userId = cursor.getInt(userIdColumnIndex);
                    int productId = cursor.getInt(proIdColumnIndex);
                    int orderQuantity = cursor.getInt(quantityColumnIndex);
                    int orderTotalPrice = cursor.getInt(totalpriceColumnIndex);
                    String orderFeedback = cursor.getString(feedbackColumnIndex);
                    int orderRating = cursor.getInt(ratingColumnIndex);

                    UserOrder userOrder = new UserOrder(orderId, userId, productId, orderQuantity, orderTotalPrice, orderFeedback, orderRating);
                    userOrders.add(userOrder);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return userOrders;
    }

    public boolean updateUserOrderFeedbackAndRating(int orderId, String feedback, int rating) {
        SQLiteDatabase myDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("feedback", feedback);
        contentValues.put("rating", rating);
        int rowsAffected = myDatabase.update("userorder", contentValues, "id=?", new String[]{String.valueOf(orderId)});
        return rowsAffected != -1;
    }
    public List<BestSellingProduct> getBestSellingProducts() {
        List<BestSellingProduct> bestSellingProducts = new ArrayList<>();
        SQLiteDatabase myDatabase = this.getReadableDatabase();

        String query = "SELECT proId, SUM(quantity) AS totalQuantity FROM userorder GROUP BY proId ORDER BY totalQuantity DESC LIMIT 5";
        Cursor cursor = myDatabase.rawQuery(query, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                int proIdColumnIndex = cursor.getColumnIndex("proId");
                int totalQuantityColumnIndex = cursor.getColumnIndex("totalQuantity");

                do {
                    int productId = cursor.getInt(proIdColumnIndex);
                    int totalQuantity = cursor.getInt(totalQuantityColumnIndex);

                    BestSellingProduct bestSellingProduct = new BestSellingProduct(productId, totalQuantity);
                    bestSellingProducts.add(bestSellingProduct);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return bestSellingProducts;
    }
}
