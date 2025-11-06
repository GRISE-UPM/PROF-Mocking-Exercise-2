## Ejercicio 2

Este código contiene una serie de clases que simulan el cambio de marchas de un coche automático. El método ``adjustGear()`` de la clase ``EngineController`` chequea la velocidad del vehículo. En función de la velocidad, se cambia la marcha (método ``setGear()``) y se loguea el cambio (método ``recordGear()``).

**Se pide:** Implementar las siguientes pruebas sobre la clase ``EngineController``, utilizando *mocks*:

*   El mensaje de log tiene el formato correcto (método ``recordGear()``).
*   Se calcula correctamente la velocidad instantánea (método ``getInstantaneousSpeed()``).
*   El método ``adjustGear`` invoca exactamente tres veces al método ``getInstantaneousSpeed()``.
*   El método ``adjustGear`` loguea la nueva marcha (método ``recordGear()``).
*   El método ``adjustGear`` asigna correctamente la nueva marcha (método ``setGear()``).

**Se entregará:** Debéis realizar un pull request al repositorio. Indicad vuestro nombre y apellidos en el comentario.
