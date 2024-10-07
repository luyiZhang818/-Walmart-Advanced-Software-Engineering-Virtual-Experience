import sqlite3
import csv

# path to database
db_path = "shipment_database.db"
csv0_path = "data/shipping_data_0.csv"
csv1_path = "data/shipping_data_1.csv"
csv2_path = "data/shipping_data_2.csv"

# process spreadsheet 0 and insert data into database
def process_shipping_data_0(cursor):
    with open(csv0_path, newline='') as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:
            # insert into database
            cursor.execute("""
                INSERT INTO shipments (shipment_identifier, product_name, quantity, shipment_date) VALUES (?, ?, ?, ?)
            """, (row['shipment_identifier'], row['product_name'], row['quantity'], row['shipment_date']))

# process spreadsheet 1 and 2 and insert data into databse
def process_shipping_data_1_2(cursor):
    # read locations from spreadsheet 2 and store as id : (from, to)
    location_data = {}
    with open(csv2_path, newline='') as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:
            shipment_id = row['shipment_identifier']
            location_data[shipment_id] = (row['origin'], row['destination'])

    # process spreadsheet 1 looking up locations
    with open(csv1_path, newline='') as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:
            shipment_id = row['shipment_identifier']
            product_name = row['product_name']
            quantity = int(row['quantity'])
            shipment_date = row['shipment_date']

            # find the from, to in location hashmap
            origin, destination = location_data.get(shipment_id, (None, None))

            # insert into database
            cursor.execute("""
                INSERT INTO shipments (shipment_identifier, product_name, quantity, shipment_date, origin, destination) VALUES (?, ?, ?, ?, ?, ?)
            """, (shipment_id, product_name, quantity, shipment_date, origin, destination))

if __name__ == "__main__":
    # connect to SQLite database
    connection = sqlite3.connect(db_path)
    cursor = connection.cursor()

    # run functions
    process_shipping_data_0(cursor)
    connection.commit()

    process_shipping_data_1_2(cursor)
    connection.commit()

    connection.close()