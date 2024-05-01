INSERT INTO EVENTS (EVENT_NAME, EVENT_DATE_TIME, LOCATION, ADDITIONAL_INFO) VALUES
                                                                                ('Tech Conference 2024', '2024-05-15 09:00:00', 'Conference Center, Tallinn', 'Annual technology conference focusing on software development.'),
                                                                                ('Art Workshop', '2024-06-20 14:00:00', 'Art Gallery, Tartu', 'A hands-on workshop for aspiring artists.'),
                                                                                ('Business Networking Event', '2024-07-05 18:00:00', 'Hotel Europa, Pärnu', 'Networking event for professionals and entrepreneurs.');

INSERT INTO INDIVIDUALS (EVENT_ID,FIRST_NAME, LAST_NAME, PERSONALID, PAYMENT_METHOD, ADDITIONAL_INFO) VALUES
                                                                                                          (1, 'John', 'Doe', '39201231234', 'bank transfer', 'Looking forward to the event!'),
                                                                                                          (1, 'Maria', 'Smith', '49005050321', 'cash', 'Please provide information about parking.'),
                                                                                                          (2, 'Julia', 'Kask', '60102030405', 'bank transfer', 'Interested in watercolor techniques.');

INSERT INTO COMPANIES (EVENT_ID, LEGAL_NAME,REGISTRATION_CODE, NUMBER_OF_PARTICIPANTS, PAYMENT_METHOD, ADDITIONAL_INFO) VALUES
                                                                                                                            (3, 'Innovative Tech OÜ', '12345678', 5, 'bank transfer', 'Please reserve a table for brochures.'),
                                                                                                                            (3, 'GreenTech AS', '87654321', 3, 'cash', 'We will bring our own name badges.'),
                                                                                                                            (2, 'Creative Minds OÜ', '23456789', 2, 'bank transfer', 'Two of our members will attend the workshop.');
