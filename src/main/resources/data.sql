INSERT INTO public.vulnerability(name, description, code)
VALUES
    ('Basic XSS',
     'A simple direct script injection into the page, causing an alert popup.',
     '<script>alert("XSS")</script>'),

    ('Image Onerror',
     'An XSS attack using the onerror attribute of an image tag to execute a script.',
     '<img src="invalid-image" onerror="alert("XSS")">'),

    ('URL Parameter',
     'An XSS where malicious JavaScript is injected through a URL parameter, which is then rendered as a clickable link that triggers JavaScript when clicked.',
     '<a href="javascript:alert(\"XSS")">Click Me</a>');


INSERT INTO public.solution(vulnerability_type, solution, implementation_steps, external_resource_link)
VALUES
    ('Basic XSS',
     'Sanitize user input and escape output to prevent script execution.',
     'Step 1. Use a library to properly sanitize user input by removing harmful code (e.g., HTMLPurifier in PHP or Dompurify in JavaScript). Step 2. Escape user input before rendering it into the webpage (e.g., use htmlspecialchars() in PHP, or escape() in JavaScript). Step 3. Apply Content Security Policy (CSP) to restrict what scripts are allowed to run on the site.',
     'https://owasp.org/www-community/attacks/xss/ https://developer.mozilla.org/en-US/docs/Web/HTTP/CSP'),

    ('Image Onerror',
     'Validate image source and disable execution of events like onerror.',
     'Step 1. Strip potentially dangerous attributes such as onerror from any user-submitted HTML. Step 2. Use a secure HTML sanitizer (e.g., DomPurify) to clean up user inputs. Step 3. Apply a whitelist of allowed HTML tags and attributes to ensure only safe content is rendered. Step 4. Use CSP headers to prevent inline script execution.',
     'https://cheatsheetseries.owasp.org/cheatsheets/DOM_based_XSS_Prevention_Cheat_Sheet.html https://owasp.org/www-community/attacks/xss/'),

    ('URL Parameter',
     'Escape special characters and encode URL parameters.',
     'Step 1. Always encode special characters (e.g., &, <, >, ", \) in user input before rendering in an HTML context (e.g., use encodeURIComponent() in JavaScript or urlencode() in PHP). Step 2. Implement a strict Content Security Policy (CSP) to prevent inline JavaScript execution. Step 3. Avoid using "javascript:" in URLs and remove it from any user-submitted links. Step 4. Use libraries like OWASP Encoder for encoding HTML, JavaScript, and URLs.',
     'https://owasp.org/www-community/attacks/xss/ https://cheatsheetseries.owasp.org/cheatsheets/XSS_Prevention_Cheat_Sheet.html');
