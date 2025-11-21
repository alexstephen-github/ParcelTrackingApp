-- Create status_history table
CREATE TABLE status_history (
    id BIGSERIAL PRIMARY KEY,
    shipment_id BIGINT NOT NULL,
    old_status VARCHAR(50),
    new_status VARCHAR(50) NOT NULL,
    location VARCHAR(255),
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (shipment_id) REFERENCES shipments(id) ON DELETE CASCADE
);

-- Create indexes
CREATE INDEX idx_shipment_id ON status_history(shipment_id);
CREATE INDEX idx_timestamp ON status_history(timestamp);
