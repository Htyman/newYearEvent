(function () {
    const form = document.querySelector('[data-login-form]');
    if (!form) return;

    const errorBox = form.querySelector('[data-login-error]');
    const successBox = form.querySelector('[data-login-success]');

    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        if (errorBox) { errorBox.textContent = ''; }
        if (successBox) { successBox.textContent = ''; }

        const login = form.querySelector('input[name="login"]').value.trim();
        const password = form.querySelector('input[name="password"]').value;

        if (!login || !password) {
            if (errorBox) errorBox.textContent = 'Введите логин и пароль.';
            return;
        }

        try {
            const resp = await fetch('/api/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ login, password })
            });

            if (!resp.ok) {
                if (resp.status === 401 || resp.status === 403) {
                    if (errorBox) errorBox.textContent = 'Неверный логин или пароль.';
                } else {
                    if (errorBox) errorBox.textContent = 'Ошибка авторизации (' + resp.status + ').';
                }
                return;
            }

            if (successBox) successBox.textContent = 'Успешный вход, перенаправляем...';
            form.reset();

            setTimeout(() => {
                window.location.href = '/nominations';
            }, 700);

        } catch (err) {
            console.error(err);
            if (errorBox) errorBox.textContent = 'Ошибка сети при авторизации.';
        }
    });
})();
