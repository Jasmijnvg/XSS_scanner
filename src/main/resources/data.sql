

-- INSERT INTO public.vulnerability(name, description, code)
-- VALUES
--     ('Basic XSS',
--      'A simple direct script injection into the page, causing an alert popup.',
--      '<script>alert("XSS")</script>'),
--
--     ('Image Onerror',
--      'An XSS attack using the onerror attribute of an image tag to execute a script.',
--      '<img src="invalid-image" onerror="alert("XSS")">'),
--
--     ('URL Parameter',
--      'An XSS where malicious JavaScript is injected through a URL parameter, which is then rendered as a clickable link that triggers JavaScript when clicked.',
--      '<a href="javascript:alert(\"XSS")">Click Me</a>');
--
--
-- INSERT INTO public.solution(vulnerability_type, solution, implementation_steps, external_resource_link)
-- VALUES
--     ('Basic XSS',
--      'Sanitize user input and escape output to prevent script execution.',
--      'Step 1. Use a library to properly sanitize user input by removing harmful code (e.g., HTMLPurifier in PHP or Dompurify in JavaScript). Step 2. Escape user input before rendering it into the webpage (e.g., use htmlspecialchars() in PHP, or escape() in JavaScript). Step 3. Apply Content Security Policy (CSP) to restrict what scripts are allowed to run on the site.',
--      'https://owasp.org/www-community/attacks/xss/ https://developer.mozilla.org/en-US/docs/Web/HTTP/CSP'),
--
--     ('Image Onerror',
--      'Validate image source and disable execution of events like onerror.',
--      'Step 1. Strip potentially dangerous attributes such as onerror from any user-submitted HTML. Step 2. Use a secure HTML sanitizer (e.g., DomPurify) to clean up user inputs. Step 3. Apply a whitelist of allowed HTML tags and attributes to ensure only safe content is rendered. Step 4. Use CSP headers to prevent inline script execution.',
--      'https://cheatsheetseries.owasp.org/cheatsheets/DOM_based_XSS_Prevention_Cheat_Sheet.html https://owasp.org/www-community/attacks/xss/'),
--
--     ('URL Parameter',
--      'Escape special characters and encode URL parameters.',
--      'Step 1. Always encode special characters (e.g., &, <, >, ", \) in user input before rendering in an HTML context (e.g., use encodeURIComponent() in JavaScript or urlencode() in PHP). Step 2. Implement a strict Content Security Policy (CSP) to prevent inline JavaScript execution. Step 3. Avoid using "javascript:" in URLs and remove it from any user-submitted links. Step 4. Use libraries like OWASP Encoder for encoding HTML, JavaScript, and URLs.',
--      'https://owasp.org/www-community/attacks/xss/ https://cheatsheetseries.owasp.org/cheatsheets/XSS_Prevention_Cheat_Sheet.html');
--
-- Vulnerabilities invoegen
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