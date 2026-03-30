const API_BASE_URL = 'http://localhost:8080/api';

// NHÚNG GIAO DIỆN TOAST VÀ MODAL XÁC NHẬN VÀO TRANG
document.body.insertAdjacentHTML('beforeend', `
    <div id="toast-container"></div>

    <div class="modal-overlay" id="customConfirmModal" style="position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(0,0,0,0.6); display:flex; align-items:center; justify-content:center; z-index:10000; visibility:hidden; opacity:0; transition:0.3s;">
        <div class="modal-box" style="background:white; width:450px; padding:30px; border-radius:12px; box-shadow:0 10px 30px rgba(0,0,0,0.3); transform:scale(0.8); transition:0.3s;">
            <h3 id="confirmTitle" style="margin-bottom:15px; color:#3a3b45; font-size:1.3rem; font-weight:700;">Xác nhận</h3>
            <p id="confirmMessage" style="margin-bottom:20px; color:#5a5c69; font-size:1rem; line-height:1.5;"></p>
            <input type="text" id="promptInput" class="form-control" style="display:none; margin-bottom:20px; width:100%; padding:12px; border:1px solid #d1d3e2; border-radius:6px; font-size:1rem;" placeholder="Nhập câu trả lời của bạn...">
            <div style="display:flex; gap:15px; justify-content:flex-end;">
                <button class="btn" style="background:#eaecf4; color:#5a5c69; font-weight:600;" id="btnConfirmCancel">Hủy bỏ</button>
                <button class="btn btn-primary" id="btnConfirmOk">Đồng ý</button>
            </div>
        </div>
    </div>
    <style>
        .modal-overlay.active { visibility:visible !important; opacity:1 !important; }
        .modal-overlay.active .modal-box { transform:scale(1) !important; }
    </style>
`);

const UI = {
    toast(message, type = 'success') {
        const container = document.getElementById('toast-container');
        const toast = document.createElement('div');
        toast.className = `custom-toast ${type}`;
        let icon = type === 'success' ? '<i class="fas fa-check-circle" style="color:#1cc88a; font-size:1.4rem;"></i>' : '<i class="fas fa-exclamation-circle" style="color:#e74a3b; font-size:1.4rem;"></i>';
        toast.innerHTML = `${icon} <span style="font-size:0.95rem;">${message}</span>`;
        container.appendChild(toast);
        setTimeout(() => {
            toast.style.animation = 'slideOut 0.3s forwards';
            setTimeout(() => toast.remove(), 300);
        }, 3000);
    },

    // Hàm tạo hộp thoại thay thế window.confirm / window.prompt / window.alert
    confirm(title, message, isPrompt = false) {
        return new Promise((resolve) => {
            const modal = document.getElementById('customConfirmModal');
            const input = document.getElementById('promptInput');

            document.getElementById('confirmTitle').innerHTML = title;
            document.getElementById('confirmMessage').innerHTML = message;

            if (isPrompt) {
                input.style.display = 'block';
                input.value = '';
                setTimeout(() => input.focus(), 100);
            } else {
                input.style.display = 'none';
            }

            modal.classList.add('active');

            document.getElementById('btnConfirmOk').onclick = () => {
                modal.classList.remove('active');
                if (isPrompt && input.value.trim() === '') {
                    UI.toast("Vui lòng không để trống!", "error");
                    resolve(null);
                } else {
                    resolve(isPrompt ? input.value : true);
                }
            };

            document.getElementById('btnConfirmCancel').onclick = () => {
                modal.classList.remove('active');
                resolve(isPrompt ? null : false);
            };
        });
    }
};

const ApiClient = {
    clearAuth() { localStorage.removeItem('user_email'); localStorage.removeItem('jwt_token'); },

    async request(endpoint, options = {}) {
        const url = `${API_BASE_URL}${endpoint}`;
        const headers = { 'Content-Type': 'application/json', ...options.headers };
        const email = localStorage.getItem('user_email');
        if (email) headers['User-Email'] = email;

        try {
            const response = await fetch(url, { ...options, headers });
            const isJson = response.headers.get('content-type')?.includes('application/json');
            const data = isJson ? await response.json() : await response.text();

            if (!response.ok) {
                if (data.status === "REQUIRE_PASSWORD_CHANGE") {
                    window.location.href = '/change-password.html';
                    return Promise.reject(data);
                }
                if (response.status === 401 || response.status === 403) {
                    UI.toast("Hết phiên đăng nhập! Vui lòng đăng nhập lại.", "error");
                    setTimeout(() => window.location.href = '/index.html', 1500);
                    return Promise.reject(data);
                }
                UI.toast(data.error || data.message || 'Có lỗi từ máy chủ', 'error');
                return Promise.reject(data);
            }
            return data;
        } catch (error) {
            UI.toast('Mất kết nối đến máy chủ Spring Boot!', 'error');
            throw error;
        }
    },
    get(endpoint) { return this.request(endpoint, { method: 'GET' }); },
    post(endpoint, body) { return this.request(endpoint, { method: 'POST', body: JSON.stringify(body) }); },
    put(endpoint, body) { return this.request(endpoint, { method: 'PUT', body: JSON.stringify(body) }); },
    delete(endpoint) { return this.request(endpoint, { method: 'DELETE' }); }
};