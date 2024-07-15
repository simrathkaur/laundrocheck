import React, { useState, useEffect } from 'react';
import axios from 'axios';
import moment from 'moment';  
import 'moment-duration-format';
import './UserEmail.css'; // Import your CSS file for styles

const UserEmail = () => {
    const [interested, setInterested] = useState(false);
    const [email, setEmail] = useState('');
    const [machines, setMachines] = useState([]);
    const [notification, setNotification] = useState(null); // State for showing pop-up notifications

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const response = await axios.get('http://localhost:8081/user-info', { withCredentials: true });
                setEmail(response.data);

                const machineResponse = await axios.get('http://localhost:8081/api/status/machine-status', { withCredentials: true });
                setMachines(machineResponse.data.map(machine => ({ ...machine, processing: false })));

                // Check if the user is already interested
                const interestResponse = await axios.get(`http://localhost:8081/api/interested/is-interested?email=${response.data}`, { withCredentials: true });
                setInterested(interestResponse.data);
            } catch (error) {
                console.error('Error fetching user data:', error.response ? error.response.data : error.message);
            }
        };

        fetchUserData();
    }, []);

    const handleMachineClick = async (machineId, action) => {
        // Set the processing state for the clicked machine
        setMachines(prevMachines => prevMachines.map(machine => 
            machine.id === machineId ? { ...machine, processing: true } : machine
        ));

        try {
            const payload = action === 'start' ? { email } : {};
            const response = await axios.put(`http://localhost:8081/api/status/machine/${machineId}/${action}`, payload, { withCredentials: true });
            setMachines(prevMachines => prevMachines.map(machine => 
                machine.id === machineId ? { ...response.data, processing: false } : machine
            ));

            // Show appropriate notification based on action
            switch (action) {
                case 'start':
                    setNotification('You will be notified if someone sees your laundry is done. Leave a bag for unloading if you are late.');
                    break;
                case 'done':
                    setNotification('Previous user has been notified.');
                    break;
                case 'unload':
                    setNotification('Previous user has been notified. If it is not you, please unload in a bag/bucket.');
                    break;
                default:
                    break;
            }

            // Clear notification after 5 seconds
            setTimeout(() => {
                setNotification(null);
            }, 5000);
        } catch (error) {
            console.error('Error updating machine status:', error.response ? error.response.data : error.message);
            // Reset processing state if there's an error
            setMachines(prevMachines => prevMachines.map(machine => 
                machine.id === machineId ? { ...machine, processing: false } : machine
            ));
        }
    };

    const calculateDuration = (startTime) => {
        const start = moment(startTime);
        const now = moment();
        const duration = moment.duration(now.diff(start));
        return duration.format('HH:mm:ss');
    };

    useEffect(() => {
        const interval = setInterval(() => {
            setMachines(machines => [...machines]);
        }, 1000);
        return () => clearInterval(interval);
    }, []);

    useEffect(() => {
        if (email) {
            axios.get(`http://localhost:8081/api/interested/is-interested?email=${email}`, { withCredentials: true })
                .then(response => {
                    console.log('Response from backend:', response.data); // Log the response data
                    setInterested(response.data === 'true'); // Example conversion if necessary
                })
                .catch(error => {
                    console.error('Error checking interest status:', error.response ? error.response.data : error.message);
                });
        }
    }, [email]);

    useEffect(() => {
        console.log(interested); // Log the updated value of interested
    }, [interested]); // Add interested as a dependency

    const handleInterestClick = async () => {
        try {
            if (interested) {
                await axios.delete('http://localhost:8081/api/interested/remove', { data: email, headers: { 'Content-Type': 'application/json' }, withCredentials: true });
                setInterested(false);
            } else {
                await axios.post('http://localhost:8081/api/interested/add', email, { headers: { 'Content-Type': 'application/json' }, withCredentials: true });
                setInterested(true);
            }
        } catch (error) {
            console.error(`Error ${interested ? 'removing' : 'adding'} interest:`, error.response ? error.response.data : error.message);
        }
    };

    return (
        <div style={{ position: 'relative' }}>
            <button onClick={handleInterestClick}>
                {interested ? 'I am not interested anymore' : 'Notify me when a machine gets free'}
            </button>
            <h1>User Email</h1>
            <p>{email ? `Logged in as: ${email}` : 'Not logged in'}</p>
            <div>
                {machines.map(machine => (
                    <div key={machine.id}>
                        <h3>Machine ID: {machine.id}</h3>
                        <p>Status: {machine.available ? 'Available' : machine.inUse ? 'In Use' : 'Done'}</p>
                        {machine.inUse && machine.startTime && (
                            <p>Washing for: {calculateDuration(machine.startTime)}</p>
                        )}
                        {machine.available && (
                            <button
                                style={{ backgroundColor: 'green' }}
                                onClick={() => handleMachineClick(machine.id, 'start')}
                                disabled={machine.processing}
                            >
                                {machine.processing ? 'Processing...' : 'Click to use'}
                            </button>
                        )}
                        {machine.inUse && (
                            <button
                                style={{ backgroundColor: 'red' }}
                                onClick={() => handleMachineClick(machine.id, 'done')}
                                disabled={machine.processing}
                            >
                                {machine.processing ? 'Notifying user...' : 'Click if machine is done'}
                            </button>
                        )}
                        {machine.done && (
                            <button
                                style={{ backgroundColor: 'yellow' }}
                                onClick={() => handleMachineClick(machine.id, 'unload')}
                                disabled={machine.processing}
                            >
                                {machine.processing ? 'Notifying user...' : 'Click if unloaded'}
                            </button>
                        )}
                    </div>
                ))}
            </div>
            {notification && (
                <div className="notification">
                    {notification}
                </div>
            )}
        </div>
    );
};

export default UserEmail;
