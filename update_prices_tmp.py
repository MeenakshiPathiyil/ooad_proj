import mysql.connector

try:
    conn = mysql.connector.connect(
        host="localhost",
        user="root",
        password="robthebobber3090$11",
        database="unisync"
    )
    cursor = conn.cursor(dictionary=True)
    
    # Check all resources that are SELL and Price = 0
    cursor.execute("SELECT ResourceId, Title, ListingType FROM Resource WHERE Price = 0")
    resources = cursor.fetchall()
    
    for r in resources:
        price = 0
        if r['ListingType'] == 'SELL':
            price = 450.00
            
        print(f"Updating '{r['Title']}' (ID: {r['ResourceId']}, Type: {r['ListingType']}) to ₹{price}")
        
        cursor.execute("UPDATE Resource SET Price = %s WHERE ResourceId = %s", (price, r['ResourceId']))
        
    conn.commit()
    print("Database prices updated successfully!")
    
except Exception as e:
    print("Error:", e)
finally:
    if 'cursor' in locals():
        cursor.close()
    if 'conn' in locals():
        conn.close()
