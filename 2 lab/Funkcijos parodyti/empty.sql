SELECT table_name
FROM information_schema.tables
WHERE table_schema = 'mido9017';

SELECT table_name
FROM information_schema.views
WHERE table_schema = 'mido9017';

SELECT routine_name
FROM information_schema.routines
WHERE routine_schema = 'mido9017';

SELECT trigger_name
FROM information_schema.triggers
WHERE event_object_schema = 'mido9017';

SELECT indexname
FROM pg_indexes
WHERE schemaname = 'mido9017';

SELECT * 
FROM pg_matviews 
WHERE schemaname = 'mido9017';