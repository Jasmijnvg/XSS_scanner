INSERT INTO public.vulnerability (name, description, code)
VALUES
    ('Basic Script Injection', 'A basic XSS vulnerability where untrusted user input is executed as script in the browser.', '<script src=".*?"></script>'),
    ('Image Onerror XSS', 'XSS vulnerability through the "onerror" attribute in an image tag.', '<img\s+src=".*?"\s+onerror="alert'),
    ('URL-based XSS', 'An XSS vulnerability where JavaScript is injected through a URL parameter.', '<a\s+href="javascript:.*?">.*?</a>'),
    ('DOM-based XSS', 'XSS that occurs in the client-side JavaScript when untrusted input is used to modify the DOM.', 'document.write(.*?)'),
    ('Reflected XSS in Form Inputs', 'Reflected XSS vulnerability that occurs when form input is not sanitized.', '<input type=".*?" .*?=".*?">');

INSERT INTO public.solution (vulnerability_type, solution, implementation_steps, external_resource_link, vulnerability_id)
VALUES
    ('Basic Script Injection', 'HTML encoding', 'Escape special characters like <, >, and & before rendering them on the page.', 'https://owasp.org/www-community/attacks/xss/', 1),
    ('Basic Script Injection', 'Content Security Policy (CSP)', 'Configure CSP headers to block inline scripts and dynamic script executions.', 'https://developer.mozilla.org/en-US/docs/Web/HTTP/CSP', 1),
    ('Basic Script Injection', 'Use a secure framework', 'Use frameworks such as React or Angular that apply automatic secure output encoding.', 'https://reactjs.org/docs/dom-elements.html', 1),

    ('Image Onerror XSS', 'Validate and sanitize image uploads', 'Validate and sanitize image uploads before displaying them on the site.', 'https://owasp.org/www-community/attacks/xss/', 2),
    ('Image Onerror XSS', 'Disable dangerous attributes', 'Remove dangerous attributes such as "onerror" when rendering user content.', 'https://owasp.org/www-community/attacks/xss/', 2),
    ('Image Onerror XSS', 'Content Security Policy (CSP)', 'Restrict inline event handlers like "onerror" by implementing a strong CSP.', 'https://developer.mozilla.org/en-US/docs/Web/HTTP/CSP', 2),

    ('URL-based XSS', 'Validate and sanitize URL parameters', 'Ensure all URL parameters are validated and sanitized before use in the application.', 'https://owasp.org/www-community/attacks/xss/', 3),
    ('URL-based XSS', 'Apply URL encoding', 'Escape harmful characters in URL parameters by applying URL encoding.', 'https://owasp.org/www-community/attacks/xss/', 3),
    ('URL-based XSS', 'Use secure HTTP headers', 'Use headers like X-XSS-Protection to prevent attacks.', 'https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-XSS-Protection', 3),

    ('DOM-based XSS', 'Avoid direct DOM manipulation', 'Avoid directly manipulating the DOM with untrusted input.', 'https://owasp.org/www-community/attacks/xss/', 4),
    ('DOM-based XSS', 'Use safe JavaScript methods', 'Use methods such as `textContent` or `setAttribute` instead of `innerHTML`.', 'https://developer.mozilla.org/en-US/docs/Web/API/Element/textContent', 4),
    ('DOM-based XSS', 'Content Security Policy (CSP)', 'Apply a CSP to restrict execution of unsafe scripts.', 'https://developer.mozilla.org/en-US/docs/Web/HTTP/CSP', 4),

    ('Reflected XSS', 'Validate form inputs server-side', 'Always perform server-side validation and sanitization of form inputs.', 'https://owasp.org/www-community/attacks/xss/', 5),
    ('Reflected XSS', 'Client-side sanitization using JavaScript', 'Use libraries like DOMPurify to sanitize input on the client-side.', 'https://github.com/cure53/DOMPurify', 5),
    ('Reflected XSS', 'Use anti-XSS tools', 'Employ tools such as OWASP AntiSamy to safely sanitize user input.', 'https://owasp.org/www-project-antisamy/', 5);

INSERT INTO roles (active, description, role_name)
VALUES
    (true, 'administrator role', 'ROLE_ADMIN'),
    (true, 'user roles', 'ROLE_USER');

INSERT INTO users (password, user_name, enabled)
VALUES
    ('$2a$12$qkWm0strF3KjcMKqGkhn7.gPNDFw3YwH2oTL44R66KH.g8ZAXosL6', 'Jasmijn', true),
    ('$2a$12$qkWm0strF3KjcMKqGkhn7.gPNDFw3YwH2oTL44R66KH.g8ZAXosL6', 'Jasmijnuser', true);

INSERT INTO user_role (user_id, role_id)
VALUES
    (1,1),
    (2,2)
