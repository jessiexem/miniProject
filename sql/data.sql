-- insert users into database
insert into user(username,password)
    values
        ('jessie', sha1('abc123')),
        ('fred', sha1('password')),
        ('wilma', sha1('123456'));