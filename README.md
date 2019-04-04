# Ebuy
An online Electronics shopping created using Java and Dropwizard framework for REST API

Following collections are used:
1. seqNumber - to generate userId and orderId
2. customer  - to store customer details (both admin and customer)
3. catalogue - to store list of products
4. order     - to store order details of customers

Following are the REST API's created:
1. /ebuy
   GET - displays a welcome message along with the version details
2. /ebuy/register
   POST - receives user details and returns success message on successful registration. user id for each user is generated.
   COLLECTION: customer
   REQUEST:
    {
      "userName":"moni",
      "password":"moni123",
      "type":"customer",
      "emailId":"moni@gmail.com"
    }
3. /ebuy/customer
   POST - receives user id and displays user details
   COLLECTION: customer
   REQUEST:
   {
	    "userId":"102"
   }
4. /ebuy/additems
   POST - allows only admin user to add items in the catalogue
   COLLECTION: catalogue
   REQUEST:
   {
      "userId":102,
      "productId": 16,
      "productName": "Laptop",
      "price": "15000",
      "quantity": 2
   }
5. /ebuy//product/catalogue
   GET - returns the items available in the catalogue
   COLLECTION: catalogue
6. /ebuy/product/purchase
   POST - based on user id and product id, purchase is made and order id is generated. 
          Quantity is deducted from catalogue after successful purchase.
   COLLECTION - order
   REQUEST:
   {
     "userId":100,
     "productId":11
   }
7. /ebuy/product/cancel
   POST - based on user id and order id, cancel of product is made.
   COLLECTION - order
   REQUEST:
   {
     "userId":100,
     "orderId":501
   }
