# Sample Markdown File

This is a sample markdown file that is transformed into the site template for Gradle Fury site plugin.

1. Create a master key. it's stored in your user profile/.gradle/fury.properties. It's an AES key that's used to encrypt all your stuff. Never check this in
2. Encrypt your password. It's project specific and stored in projectRoot/local.properties. Don't check this is, but if you do, it's not a big deal since it's encrypted.
3. When you build your project, local.properties is merged into the current context (which includes gradle.properties). The credentials are then decrypted when needed.

The cipher text is stored as key={CIPHERTEXT}. The opening and closing brackets are used internal to determine if something could be encrypted. "key" is the normal set of properties keys used by fury (and android)