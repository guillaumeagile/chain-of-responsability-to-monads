# De la Programmation Orientée Objet à la Programmation Fonctionnelle : Le Pattern Chain of Responsibility

## 📚 Objectif Pédagogique

Ce projet illustre comment **migrer progressivement** d'une approche **Orientée Objet (OO)** classique vers une approche **Fonctionnelle (FP)** en Java, en utilisant le pattern **Chain of Responsibility** comme cas d'étude pratique.

L'intention est de montrer que cette transition n'est **pas un saut radical**, mais une **évolution naturelle** où les principes FP apportent plus de clarté, de testabilité et de maintenabilité.

---

## 🏗️ Structure du Projet

### 1️⃣ Approche Orientée Objet : `chain_of_responsibilities/`

**Concept** : Chaque étape du traitement est une classe qui hérite d'un handler de base et délègue au suivant.

```
Request → [AuthenticationHandler] → [AuthorizationHandler] → [DataValidationHandler] → Result
```

**Caractéristiques OO** :
- 🤔 Héritage avec `BaseRequestHandler` (beaucoup de boilerplate objets/classes)
- 🤔 État mutable (`nextHandler`)  (plus difficile de raisonner sur les effets de bord)
- 🤔 Chaînage explicite via `setNext()`  (facile à corrompre, voir le test qui échoue)
- ❌ Couplage entre les handlers
- ❌ Gestion d'erreurs par exceptions
- ❌ Difficile à tester en isolation

**Problème identifié** : Dans `DataValidationHandler`, le handler retourne simplement `", validation: Ok"` sans vraiment valider les données. C'est un exemple du couplage faible et de la responsabilité mal distribuée.

### 2️⃣ Approche Fonctionnelle : `monads/vavr/`

**Concept** : Chaque étape est une **fonction pure** qui retourne un `Either<Error, Value>`. Les étapes se composent avec `flatMap()`.

```java
ChainOfMonads.validateRequest(request)
    .flatMap(ChainOfMonads::authenticateRequest)
    .flatMap(ChainOfMonads::authorizeRequest)
    .flatMap(ChainOfMonads::processBusinessLogic)
```

**Caractéristiques FP** :
- ✅ Fonctions pures (pas d'effet de bord)
- ✅ Immutabilité
- ✅ Composition avec `flatMap()` (monades)
- ✅ Gestion d'erreurs explicite avec `Either`
- ✅ Pas de couplage entre les fonctions
- ✅ Testabilité maximale

---

## 🔄 Comparaison : OO vs FP

| Aspect | OO (Chain of Responsibility) | FP (Monades avec Vavr) |
|--------|------------------------------|------------------------|
| **Unité de base** | Classes avec état | Fonctions pures |
| **Composition** | Héritage + délégation | `flatMap()` |
| **Erreurs** | Exceptions (try/catch) | `Either<Error, Value>` |
| **Testabilité** | Difficile (dépendances) | Triviale (fonctions) |
| **Lisibilité** | Implicite (chaînage) | Explicite (pipeline) |
| **Flexibilité** | Rigide (hiérarchie) | Flexible (composition) |

---

## 💡 Avantages de l'Approche Fonctionnelle

### 1. **Gestion d'Erreurs Explicite**
```java
// OO : exceptions implicites
try {
    handler.handleRequest(request);
} catch (Exception e) {
    // Où vient l'erreur ? De quel handler ?
}

// FP : erreurs explicites dans le type
Either<ProcessingError, String> result = chainOfMonads.processChain(request);
if (result.isLeft()) {
    ProcessingError error = result.getLeft();
    // Type, message, contexte : tout est clair
}
```

### 2. **Composition Transparente**
```java
// FP : chaque étape est indépendante et réutilisable
Either<ProcessingError, Request> validated = ChainOfMonads.validateRequest(request);
Either<ProcessingError, Request> authenticated = validated.flatMap(ChainOfMonads::authenticateRequest);
// Ou en une ligne :
Either<ProcessingError, String> result = 
    validateRequest(request)
        .flatMap(ChainOfMonads::authenticateRequest)
        .flatMap(ChainOfMonads::authorizeRequest)
        .flatMap(ChainOfMonads::processBusinessLogic);
```

### 3. **Testabilité**
```java
// Chaque fonction est testable indépendamment
@Test
public void testValidationFailure() {
    var result = ChainOfMonads.validateRequest(new Request("John", "admin", ""));
    assertThat(result).isLeft();
    assertThat(result.getLeft().type()).isEqualTo(ErrorType.VALIDATION);
}
```

### 4. **Pas d'Effets de Bord**
```java
// FP : aucun état global modifié
public static Either<ProcessingError, Request> validateRequest(Request request) {
    if (request.data() == null || request.data().isEmpty()) {
        return Either.left(new ProcessingError(ErrorType.VALIDATION, "Data is empty"));
    }
    return Either.right(request);
}
// Appelée 100 fois avec les mêmes paramètres = même résultat
```

---

## 🎯 Étapes de Migration OO → FP

### Phase 1 : Comprendre les Monades
- Lire les commentaires dans `ChainOfMonads.java`
- Comprendre que `flatMap()` = "appliquer une fonction qui retourne un `Either`"
- Voir que le premier `Either.left()` court-circuite toute la chaîne

### Phase 2 : Remplacer les Exceptions
- Remplacer `try/catch` par `Either`
- Chaque fonction retourne `Either<Error, Value>` au lieu de lever une exception

### Phase 3 : Composer avec `flatMap()`
- Au lieu de `handler1.setNext(handler2)`, utiliser `result.flatMap(handler2)`
- La composition devient **explicite et lisible**

### Phase 4 : Tester avec AssertJ-Vavr
- Utiliser `assertThat(result).isRight()` / `isLeft()`
- Assertions fluides et lisibles pour les types Vavr

---

## 🧪 Tests Modernes avec AssertJ-Vavr

Ce projet utilise **AssertJ** et **AssertJ-Vavr** pour des assertions fluides et modernes :

```java
// Assertions fluides pour Either
assertThat(result)
    .isRight()
    .extracting(Either::get)
    .isEqualTo("Finally: Successfully processed request for: John");

// Assertions pour les erreurs
assertThat(result)
    .isLeft()
    .extracting(Either::getLeft)
    .extracting(ProcessingError::type)
    .isEqualTo(ErrorType.VALIDATION);
```

---

## 📦 Dépendances Clés

- **Vavr** : Structures de données fonctionnelles (`Either`, `Option`, `Try`, etc.)
- **AssertJ** : Assertions fluides
- **AssertJ-Vavr** : Support des types Vavr dans AssertJ
- **JUnit 5** : Framework de test moderne

---

## 🚀 Exécution

```bash
# Compiler
./gradlew build

# Lancer les tests
./gradlew test

# Voir les résultats
open build/reports/tests/test/index.html
```

---

## 📝 Leçons Clés

1. **Les monades ne sont pas magiques** : `Either` est juste un conteneur qui force à gérer les erreurs explicitement
2. **`flatMap()` est votre ami** : Il permet de composer des opérations qui peuvent échouer
3. **Les fonctions pures sont testables** : Pas d'état global, pas de dépendances cachées
4. **La FP n'exclut pas l'OO** : Vous pouvez les mélanger progressivement
5. **Les types sont vos alliés** : `Either<Error, Value>` force à penser aux deux cas

---

## 🎓 Pour Aller Plus Loin

- Lire les commentaires dans `ChainOfMonads.java` sur `flatMap()`
- Comparer les tests OO (`chain_of_responsibilites/`) et FP (`monads/vavr/`)
- Essayer d'ajouter une nouvelle étape de validation sans modifier les existantes (FP gagne !)
- Explorer d'autres monades : `Option`, `Try`, `Validation`
