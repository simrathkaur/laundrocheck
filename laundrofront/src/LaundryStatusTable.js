import React, { useEffect, useState } from 'react';
import axios from 'axios';

const LaundryStatusTable = () => {
    const [statuses, setStatuses] = useState([]);

    useEffect(() => {
        fetchStatuses();
    }, []);

    const fetchStatuses = async () => {
        const response = await axios.get('/api/status');
        setStatuses(response.data);
    };

    const updateStatus = async (id, action) => {
        const response = await axios.post(`/api/status/${id}/${action}`);
        setStatuses(statuses.map(status => status.id === id ? response.data : status));
    };

    const getStatusButton = (status) => {
        if (status.available) {
            return <button style={{ backgroundColor: 'green' }} onClick={() => updateStatus(status.id, 'inUse')}>Available</button>;
        } else if (status.inUse) {
            return <button style={{ backgroundColor: 'red' }} onClick={() => updateStatus(status.id, 'done')}>In Use</button>;
        } else if (status.done) {
            return <button style={{ backgroundColor: 'yellow' }} onClick={() => updateStatus(status.id, 'available')}>Done</button>;
        }
    };

    return (
        <table>
            <thead>
                <tr>
                    <th>User Email</th>
                    <th>Start Time</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
                {statuses.map(status => (
                    <tr key={status.id}>
                        <td>{status.userEmail}</td>
                        <td>{new Date(status.startTime).toLocaleString()}</td>
                        <td>{getStatusButton(status)}</td>
                    </tr>
                ))}
            </tbody>
        </table>
    );
};

export default LaundryStatusTable;
