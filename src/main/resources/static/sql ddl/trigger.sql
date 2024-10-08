CREATE TABLE invoice_details_log (
    log_id SERIAL PRIMARY KEY,
    id BIGINT,
    invoice_no BIGINT,
    invoice_date DATE,
    date_of_service DATE,
    buyer_id INT,
    billing_to VARCHAR(255),
    gst_applicable VARCHAR(10),
    gst_no VARCHAR(50),
    client_name VARCHAR(255),
    client_address TEXT,
    origin VARCHAR(255),
    destination VARCHAR(255),
    no_of_days INT,
    vehicle_no VARCHAR(50),
    vehicle_type VARCHAR(255),
    driver_name VARCHAR(255),
    total_hrs DECIMAL(10,2),
    actual_hrs DECIMAL(10,2),
    extra_hrs DECIMAL(10,2),
    basic_km DECIMAL(10,2),
    base_fare DECIMAL(10,2),
    actual_km DECIMAL(10,2),
    extra_km DECIMAL(10,2),
    extra_per_km_rate DECIMAL(10,2),
    extra_per_hr_rate DECIMAL(10,2),
    extra_hr_rate DECIMAL(10,2),
    extra_km_rate DECIMAL(10,2),
    food_allowance DECIMAL(10,2),
    toll_and_parking DECIMAL(10,2),
    parking DECIMAL(10,2),
    net_amount DECIMAL(10,2),
    gst_amount DECIMAL(10,2),
    total_amount_including_gst DECIMAL(10,2),
    grand_net_amount_inwords VARCHAR(255),
    created_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP,
    updated_by VARCHAR(255),
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- When the update occurred
    update_reason VARCHAR(255)  -- Reason for the update
);
CREATE OR REPLACE FUNCTION log_invoice_details_update()
RETURNS TRIGGER AS $$
BEGIN
    -- Insert the updated row into the log table
    INSERT INTO invoice_details_log(
        id, invoice_no, invoice_date, date_of_service, buyer_id, billing_to, gst_applicable, gst_no,
        client_name, client_address, origin, destination, no_of_days, vehicle_no, vehicle_type,
        driver_name, total_hrs, actual_hrs, extra_hrs, basic_km, base_fare, actual_km, extra_km,
        extra_per_km_rate, extra_per_hr_rate, extra_hr_rate, extra_km_rate, food_allowance, toll_and_parking,
        parking, net_amount, gst_amount, total_amount_including_gst, grand_net_amount_inwords,
        created_at, created_by, updated_at, updated_by, update_time, update_reason
    )
    VALUES (
        OLD.id, OLD.invoice_no, OLD.invoice_date, OLD.date_of_service, OLD.buyer_id, OLD.billing_to,
        OLD.gst_applicable, OLD.gst_no, OLD.client_name, OLD.client_address, OLD.origin, OLD.destination,
        OLD.no_of_days, OLD.vehicle_no, OLD.vehicle_type, OLD.driver_name, OLD.total_hrs, OLD.actual_hrs,
        OLD.extra_hrs, OLD.basic_km, OLD.base_fare, OLD.actual_km, OLD.extra_km, OLD.extra_per_km_rate,
        OLD.extra_per_hr_rate, OLD.extra_hr_rate, OLD.extra_km_rate, OLD.food_allowance, OLD.toll_and_parking,
        OLD.parking, OLD.net_amount, OLD.gst_amount, OLD.total_amount_including_gst, OLD.grand_net_amount_inwords,
        OLD.created_at, OLD.created_by, OLD.updated_at, OLD.updated_by,
        CURRENT_TIMESTAMP, TG_ARGV[0]  -- Current timestamp and update reason
    );

    RETURN NEW;  -- Continue with the update operation
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER invoice_details_update_trigger
AFTER UPDATE ON invoice_detailss
FOR EACH ROW
EXECUTE FUNCTION log_invoice_details_update('Update reason: Information changed');