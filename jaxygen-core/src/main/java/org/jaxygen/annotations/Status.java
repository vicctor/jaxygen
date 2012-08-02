/*
 * Copyright 2012 Artur Keska.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jaxygen.annotations;

/**Status of the implemention of given method or class
 *
 * @author Artur Keska
 */
  public enum Status  {
    Undefined,         /** The status has not been defined by the developer */
    Placeholder,       /** The method is just a placeholder to keep in mind that probably one needed sych method */
    Mockup,            /** Method is implemened just as a mockup. It gives a result which fullfills the application design rules */
    Nonfunctional,     /** Method is implemented but not functional */
    ReleaseCandidate,  /** Method is implemented and realy for testing */
    GenerallyAvailable /** Method is implemented and accepted by testers team */
  }