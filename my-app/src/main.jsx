import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx' // Import component App của bạn
import './style.css'

// Code này sẽ chạy App.jsx và chèn nó vào <div id="root">
ReactDOM.createRoot(document.getElementById('root')).render(
    <React.StrictMode>
        <App />
    </React.StrictMode>,
)