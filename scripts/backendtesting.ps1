#login
curl -X POST http://localhost:8080/auth/login -H "Content-Type: application/json" -d '{"username":"alice","password":"alice123"}'
#output : {"token":"ca41f924-2ba3-40ce-aa87-a72795e9c514","userId":1,"username":"alice"}
#result : pass 
#View empty cart
curl http://localhost:8080/cart -H "Authorization: Bearer ca41f924-2ba3-40ce-aa87-a72795e9c514"
#output : {"total":0.0,"items":[]}
#result : pass
# Add 2 laptops (productId=1)
curl -X POST http://localhost:8080/cart/add -H "Authorization: Bearer ca41f924-2ba3-40ce-aa87-a72795e9c514" -H "Content-Type: application/json" -d '{"productId":1,"quantity":2}'
#output : {"1":2}
#result : pass
# Add 1 phone (productId=2)
curl -X POST http://localhost:8080/cart/add -H "Authorization: Bearer ca41f924-2ba3-40ce-aa87-a72795e9c514" -H "Content-Type: application/json" -d '{"productId":2,"quantity":1}'
#output : {"1":2,"2":1}
#result : pass
#View cart with 2 items
curl http://localhost:8080/cart -H "Authorization: Bearer ca41f924-2ba3-40ce-aa87-a72795e9c514"
#output : {"total":2599.9700000000003,"items":[{"lineTotal":1999.98,"quantity":2,"productId":1,"price":999.99,"name":"Laptop"},{"lineTotal":599.99,"quantity":1,"productId":2,"price":599.99,"name":"Phone"}]}
#result : pass
#  Remove laptop (productId=1)
curl -X DELETE http://localhost:8080/cart/remove/1 -H "Authorization: Bearer ca41f924-2ba3-40ce-aa87-a72795e9c514"
#output : {"2":1} 
#result : pass
#View cart after removal
curl http://localhost:8080/cart -H "Authorization: Bearer ca41f924-2ba3-40ce-aa87-a72795e9c514"
# output: {"total":599.99,"items":[{"lineTotal":599.99,"quantity":1,"productId":2,"price":599.99,"name":"Phone"}]}
#result : pass
#Error: negative quantity
curl -X POST http://localhost:8080/cart/add -H "Authorization: Bearer ca41f924-2ba3-40ce-aa87-a72795e9c514" -H "Content-Type: application/json" -d '{"productId":1,"quantity":-5}'
#output : "Quantity must be positive"
#result : pass
#Error: product not found
curl -X POST http://localhost:8080/cart/add -H "Authorization: Bearer ca41f924-2ba3-40ce-aa87-a72795e9c514" -H "Content-Type: application/json" -d '{"productId":999,"quantity":1}'
#output : "Product not found"
#result : pass
# No token (unauthenticated)
curl http://localhost:8080/cart
#output : {"error":"missing token"}
#result : pass
