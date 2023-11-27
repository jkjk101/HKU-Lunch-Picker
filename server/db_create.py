import sqlite3

con = sqlite3.connect('hkulunchpicker-db.db')

# create table
con.execute("""CREATE TABLE IF NOT EXISTS RESTAURANTS (
        restaurant_id INTEGER PRIMARY KEY NOT NULL,
        restaurant_name TEXT NOT NULL,
        category TEXT NOT NULL,
        price TEXT NOT NULL,
        distance INTEGER NOT NULL ,
        last_posted TEXT
    );""")


recordsToInsert = [(1, 'Po Kee BBQ Restaurant', 'East Asian', '<=50', 800, None),
                   (2, 'Chorland Cookfood Stall', 'East Asian', '100-200', 600, None),
                   (3, 'Aya', 'East Asian', '50-100', 850, None),
                   (4, 'Katsuisen', 'East Asian', '100-200', 850, None),
                   (5, 'Dare', 'Western', '50-100', 800, None),
                   (6, '#24 Foodstation', 'Western', '50-100', 800, None),
                   (7, 'Saizeriya Italian Restaurant', 'East Asian', '50-100', 450, None),
                   (8, 'Domon Izakaya', 'East Asian', '50-100', 450, None),
                   (9, 'Baan Thai', 'Southeast Asian', '100-200', 550, None),
                   (10, 'PHD', 'Western', '50-100', 700, None),
                   (11, 'MOS burger', 'Western', '50-100', 900, None),
                   (12, 'McDonalds', 'Western', '<=50', 650, None),
                   (13, 'Alfafa Café', 'Western', '<=50', 80, None),
                   (14, 'Bijas Vegetarian', 'Western', '<=50', 90, None),
                   (15, 'Po Lin Yuen Vegetarian Food', 'East Asian', '50-100', 750, None),
                   (16, 'Ka Hing Restaurant', 'East Asian', '<=50', 750, None),
                   (17, 'HKU Cym Canteen', 'East Asian', '50-100', 500, None),
                   (18, 'SuperSandwich', 'Western', '50-100', 90, None),
                   (19, 'Viet Chiu Vietnamese Restaurant', 'Southeast Asian', '50-100', 850, None),
                   (20, 'The Bridge Lounge', 'Western', '50-100', 80, None),
                   (21, 'Hot Pot Land', 'East Asian', '200-400', 650, None),
                   (22, 'Thalassic Thai Restaurant', 'East Asian', '100-200', 1300, None),
                   (23, 'New Korean B.B.Q. Restaurant', 'East Asian', '100-200', 1200, None)]


# insert data    
for data in recordsToInsert:
    sqlite_insert_query = """INSERT OR IGNORE INTO RESTAURANTS
                          (restaurant_id, restaurant_name, category, price, distance, last_posted) 
                          VALUES (?, ?, ?, ?, ?, ?);"""

    con.execute(sqlite_insert_query, data)

con.commit()
con.close()


