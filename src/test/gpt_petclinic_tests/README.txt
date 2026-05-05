AI-generated test suite for Spring PetClinic

How to use:
1. Open your project root.
2. Copy the contents under ai_petclinic_tests/src/test/java/ into your project's src/test/java/.
3. Run your normal Maven or Gradle test command.

Packages covered:
- owner
- service
- system
- vet

Files included:
- owner/AiOwnerDomainGeneratedTests.java
- owner/AiOwnerControllerGeneratedTests.java
- owner/AiPetControllerGeneratedTests.java
- owner/AiVisitControllerGeneratedTests.java
- service/AiClinicServiceGeneratedTests.java
- system/AiWebConfigurationGeneratedTests.java
- system/AiSystemControllerGeneratedTests.java
- system/AiWelcomeControllerGeneratedTests.java
- vet/AiVetGeneratedTests.java
- vet/AiVetControllerGeneratedTests.java

Notes:
- These tests are intentionally named with the Ai...GeneratedTests prefix so you can compare them with other AI-generated suites.
- They are written as a standalone suite, not as patches to your existing tests.
- I could not execute Maven in this environment because the wrapper attempted to download Maven from the internet, which is blocked here. So these files were prepared from static analysis of your uploaded project and aligned with the current project structure and existing test style.
