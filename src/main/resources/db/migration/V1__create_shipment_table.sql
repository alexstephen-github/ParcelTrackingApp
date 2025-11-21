-- Create shipments table
CREATE TABLE shipments (
    id BIGSERIAL PRIMARY KEY,
    tracking_id VARCHAR(15) UNIQUE NOT NULL,
    status VARCHAR(50) NOT NULL,
    current_location VARCHAR(255),
    origin VARCHAR(255) NOT NULL,
    destination VARCHAR(255) NOT NULL,
    customer_email VARCHAR(100),
    customer_phone VARCHAR(20),
    estimated_delivery DATE,
    last_update TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_tracking_id ON shipments(tracking_id);
CREATE INDEX idx_status ON shipments(status);
CREATE INDEX idx_customer_email ON shipments(customer_email);
