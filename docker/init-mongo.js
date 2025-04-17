db = db.getSiblingDB('interview');
db.createUser({
    user: 'mongouser',
    pwd: 'mongopw',
    roles: [
        {
            role: 'readWrite',
            db: 'interview'
        }
    ]
});