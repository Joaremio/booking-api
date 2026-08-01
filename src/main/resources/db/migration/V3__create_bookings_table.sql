CREATE TABLE bookings (
                          id UUID PRIMARY KEY,
                          user_id UUID NOT NULL REFERENCES users(id),
                          resource_id UUID NOT NULL REFERENCES resources(id),
                          start_date_time TIMESTAMP NOT NULL,
                          end_date_time TIMESTAMP NOT NULL,
                          status VARCHAR(20) NOT NULL,
                          created_at TIMESTAMP NOT NULL,
                          updated_at TIMESTAMP,
                          version BIGINT NOT NULL DEFAULT 0,

                          EXCLUDE USING gist (
                                resource_id WITH =,
                                tsrange(start_date_time, end_date_time) WITH &&
                        ) WHERE (status != 'CANCELLED')
);