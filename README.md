
# Book Bazaar Ecommerce App

The "Book Bazaar" Android application is a platform designed to facilitate buying and selling of books online. The app allows users to create accounts either as sellers or buyers. Buyers can create accounts to browse through the catalog of available books, make purchases, and read book previews or full PDFs directly within the app.


## Table Contant
 - [Features]([https://github.com/subhadip420/Book-Bazaar-E-commerce-App/edit/master/README.md#features](https://github.com/subhadip420/Book-Bazaar-E-commerce-App/blob/master/README.md#features))
 - [Screenshots]()
 - [Prerequirement]()
 - [Compatibility](https://github.com/subhadip420/Book-Bazaar-E-commerce-App/edit/master/README.md#customer)
 - [Build Setup]()
 - [Dependencies]()
 - [License]()
 - [Contact]()
 - [Acknowledgements]()
## Features

- **CUSTOMER**
   -
    - **Home**
        - Login/Signin
        - Forget Password
        - Home
        - Categories
        - Search
        - Book PDF
    - **My Information**
        - Wishlist
        - Cart
        - My Order
        - Profile Setting
    - **Communication**
        - Customer Service
        - Term & Condition
        - Shell on Book Bazaar
        - Share
    - **Logout**
  
- **SELLER**
   -
    - **Dashboard**
        - Login/Signin
        - Edit Book Details
        - Apply Changes
        - Out of Stock
        - Delete Books
    - **Add Books**
        - Upload Book Images
        - Add Book Details
        - Add Book
    - **Profile**
       - Seller Details
       - Edit Details
       - Logout
       - Customer Care

- **ADMIN**
   -
    - **Maintain Books**
        - Login/Signin
        - Edit Book Details
        - Apply Changes
        - Delete Books
    - **Approve New Books**
        - Approve Book
        - Reject Book
        - Contact to Seller
    - **Check Customer Order**
       - Contact to Customer
       - View Order Items
       - Shipped Customer Order
    - **Maintain or add PDF**
       - Edit PDF Details
       - Add PDF
    - **Logout**






## Screenshots
![Screenshot_2024-05-13-10-27-44-31_e8e8461893d2fb90fad0142e02c7ed17](https://github.com/subhadip420/Book-Bazaar-E-commerce-App/assets/136852368/a305008f-5e93-412c-9f58-271ed33f13ff)
![Screenshot_2024-05-13-11-10-51-18_e8e8461893d2fb90fad0142e02c7ed17](https://github.com/subhadip420/Book-Bazaar-E-commerce-App/assets/136852368/76af2f7c-6f8f-49e8-8d72-e5394a7aeb87)
![Screenshot_2024-05-13-10-22-55-15_e8e8461893d2fb90fad0142e02c7ed17](https://github.com/subhadip420/Book-Bazaar-E-commerce-App/assets/136852368/4b7ad45f-79ff-484b-aa4c-6a1d1cd45711)

## Prerequirement

 - [Install Android Studio](https://developer.android.com/studio)

  - [Create Account in Firebase Database](https://firebase.google.com/)
## Compatibility
- **Compile SDK:** 34
- **Minimum SDK:** 26
- **Target SDK:** 33
- **Device Type Supported:** Phone and Tab
- **Orientation supported:** Portrait and Landscape

## Build Setup
- **Step1:** Connect Your Android Studio Project to Firebase.
- **Step2:** Update Firebase Realtime Database Rule.

```bash
{
  "rules": {
    ".read": true,  
    ".write": true 
  }
}
```

- **Step2:** Update Firebase Realtime Database Rule.

```bash
rules_version = '2';

// Craft rules based on data in your Firestore database
// allow write: if firestore.get(
//    /databases/(default)/documents/users/$(request.auth.uid)).data.isAdmin;
service firebase.storage {
  match /b/{bucket}/o {

    // This rule allows anyone with your Storage bucket reference to view, edit,
    // and delete all data in your Storage bucket. It is useful for getting
    // started, but it is configured to expire after 30 days because it
    // leaves your app open to attackers. At that time, all client
    // requests to your Storage bucket will be denied.
    //
    // Make sure to write security rules for your app before that time, or else
    // all client requests to your Storage bucket will be denied until you Update
    // your rules
    match /{allPaths=**} 
    {
      allow read, write;
    }
  }
}
```
## Dependencies
All Required Dependencies

```bash
dependencies {
// AndroidX libraries
    implementation 'androidx.appcompat:appcompat:1.6.1'  // Support library for AndroidX
    implementation 'com.google.android.material:material:1.11.0'  // Material Design components for AndroidX
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'  // Layout manager for AndroidX
    
    // Firebase libraries
    implementation platform('com.google.firebase:firebase-bom:32.7.2')  // Firebase Bill of Materials
    implementation 'com.google.firebase:firebase-database:8.0.2'   // Firebase Realtime Database
    implementation 'com.google.firebase:firebase-storage:8.0.2'  // For Firebase Storage
    implementation 'com.google.firebase:firebase-auth:8.0.2'    // Firebase Authentication
    implementation 'com.firebaseui:firebase-ui-database:8.0.2'  // Firebase UI for Realtime Database
    
    // Image loading and manipulation libraries
    implementation 'com.squareup.picasso:picasso:2.8'  // Image loading and caching library
    implementation 'de.hdodenhof:circleimageview:3.1.0'  // Circular ImageView library
    implementation 'com.theartofdev.edmodo:android-image-cropper:2.8.0'  // Image cropping library
    
    // UI components and utilities
    implementation 'com.airbnb.android:lottie:6.3.0'    //Lottie animations
    implementation 'com.github.rey5137:material:1.3.1'  // Material Design components
    implementation 'io.github.pilgr:paperdb:2.7.2'    // Lightweight NoSQL database
    implementation 'com.cepheuen.elegant-number-button:lib:1.0.2'  // Custom number input buttons
    implementation 'com.github.AtikulSoftware:smart-slider:1.0'  // Customizable slider component
    
    implementation 'androidx.cardview:cardview:1.0.0'
    implementation  'androidx.recyclerview:recyclerview:1.3.2'
    
    // Navigation components
    implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.3.1'   // LiveData support library
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1'  // ViewModel support library
    implementation 'androidx.navigation:navigation-fragment:2.3.5'     // Navigation fragment library
    implementation 'androidx.navigation:navigation-ui:2.3.5'            // Navigation UI library
    
    // Testing dependencies
    testImplementation 'junit:junit:4.13.2'  // JUnit testing framework
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'  // JUnit testing extension for Android
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'  // Espresso UI testing library
}
```
## Contact
[![portfolio](https://img.shields.io/badge/my_portfolio-000?style=for-the-badge&logo=ko-fi&logoColor=white)](https://katherineoelsner.com/)
[![linkedin](https://img.shields.io/badge/linkedin-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/)
[![twitter](https://img.shields.io/badge/twitter-1DA1F2?style=for-the-badge&logo=twitter&logoColor=white)](https://twitter.com/)


## Helpfull Video Tutorial



 - [Main Concept](https://youtube.com/playlist?list=PLxefhmF0pcPlqmH_VfWneUjfuqhreUz-O&si=cayZ4w-6U4Fb4Ajf)

 - [Android Navigation Drawer Menu](https://youtu.be/D5Ha9S5SVsw?si=DC-cxIcdNf6sj1kC)

 - [Recycler View in Android Studio](https://youtube.com/playlist?list=PLirRGafa75rSMDp5bORq_eHjMLKqJ2EYO&si=CZTPUqWHEb8K4Vtr)

  - [BottomNavigationView in Android Studio](https://youtu.be/KJhYqe04bGo?si=nDuDa6AVOVhcp-Gp)

- [How to Design Custom Toolbar of App in Android Studio](https://youtu.be/o35gogTi8lY?si=ymevyYz1sr8IhPDt)

 - [Multiple RecyclerViews in One Screen Using ScrollView](https://youtu.be/CXfXFHuQIWo?si=G-RCKcSNqLOeRNlO)

 - [Show List Items In RecyclerView](https://youtu.be/DlaSiftrWeA?si=9F2GZWrQFz4b2nFT)

 - [TextInputEditText - Material Design Edit Text](https://youtu.be/CXfXFHuQIWo?si=G-RCKcSNqLOeRNlO)

 - [Send WhatsApp Message From Android App](https://youtu.be/vykAvFlSeVQ?si=Fo9oDIi31JPMc9o_)  

  - [Splash Screen - Android Studio Tutorial](https://youtu.be/Q0gRqbtFLcw?si=VfAVSLav277ZidNt)  

 - [Lottie Animation in Android Application](https://youtu.be/ccpm3X74LcY?si=yZGgxm-0IueTn33n)  

- [Auto Image Slider - Android Studio Tutorial](https://youtu.be/qeXFuOx0oJA?si=10o7WwA-lwshjL4K)  
