DO $$
DECLARE
    cname text;
BEGIN
    SELECT conname INTO cname
    FROM pg_constraint
    WHERE conrelid = 'bookings'::regclass
      AND contype = 'x'
    LIMIT 1;

    IF cname IS NOT NULL THEN
        EXECUTE format('ALTER TABLE bookings DROP CONSTRAINT %I', cname);
    END IF;
END $$;

ALTER TABLE bookings
    ADD CONSTRAINT bookings_resource_id_tsrange_excl
        EXCLUDE USING gist (
            resource_id WITH =,
            tsrange(start_date_time, end_date_time) WITH &&
        ) WHERE (status != 'CANCELADO');