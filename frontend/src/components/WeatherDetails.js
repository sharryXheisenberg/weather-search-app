import React from 'react';

function WeatherDetails({ data }) {
  const { main, wind, visibility } = data;

  const details = [
    {
      label: 'Humidity',
      value: `${main.humidity}%`,
      icon: '💧'
    },
    {
      label: 'Wind Speed',
      value: `${wind.speed} m/s`,
      icon: '💨'
    },
    {
      label: 'Pressure',
      value: `${main.pressure} hPa`,
      icon: '🌡️'
    },
    {
      label: 'Visibility',
      value: `${(visibility / 1000).toFixed(1)} km`,
      icon: '👁️'
    }
  ];

  return (
    <div className="weather-details">
      <div className="details-grid">
        {details.map((detail, index) => (
          <div key={index} className="detail-card">
            <div className="detail-icon">{detail.icon}</div>
            <div className="detail-info">
              <p className="detail-label">{detail.label}</p>
              <p className="detail-value">{detail.value}</p>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default WeatherDetails;