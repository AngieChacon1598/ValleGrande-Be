import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

import '../models/product_detail.dart';
import '../models/sales_ticket.dart';
import '../models/category.dart' as cat;
import '../models/product.dart';

final storage = FlutterSecureStorage();

class SaleDialog extends StatefulWidget {
  final String title;
  final void Function(SalesTicket) onSubmit;

  const SaleDialog({Key? key, required this.title, required this.onSubmit})
      : super(key: key);

  @override
  _SaleDialogState createState() => _SaleDialogState();
}

class _SaleDialogState extends State<SaleDialog> {
  final _formKey = GlobalKey<FormState>();
  final _noteController = TextEditingController();
  final _deliveryAddressController = TextEditingController();
  String _selectedDelivery = 'N';
  int? _selectedUserId = 1; // Simulado
  int? _selectedIdTypeState = 1; // Simulado
  int? _selectedIdPaymentType = 1; // Simulado

  List<_ProductDetailInputController> _productDetailControllers = [];
  List<Product> _products = [];
  double _calculatedTotal = 0.0;
  bool _loadingProducts = true;

  @override
  void initState() {
    super.initState();
    _loadProducts();
    _addProductDetailField();
  }

  Future<void> _loadProducts() async {
    setState(() {
      _loadingProducts = true;
    });
    try {
      final token = await storage.read(key: 'jwt_token');
      if (token == null) throw Exception('No hay token, inicia sesión');

      final response = await http.get(
        Uri.parse('https://vallegrande-be.onrender.com/api/products'),
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer $token',
        },
      );

      if (response.statusCode == 200) {
        final List<dynamic> data = jsonDecode(response.body);
        setState(() {
          _products = data.map((item) => Product.fromJson(item)).toList();
          _loadingProducts = false;
        });
      } else if (response.statusCode == 401) {
        setState(() => _loadingProducts = false);
        throw Exception('No autorizado. Token inválido o expirado.');
      } else {
        setState(() => _loadingProducts = false);
        throw Exception('Error al obtener productos:  [${response.body}');
      }
    } catch (e) {
      setState(() => _loadingProducts = false);
      print('Error al cargar productos: $e');
      // Puedes mostrar un mensaje de error si lo deseas
    }
  }

  void _recalculateTotal() {
    double total = 0.0;
    for (final controller in _productDetailControllers) {
      final productId = int.tryParse(controller.productIdController.text);
      final amount = int.tryParse(controller.amountController.text);
      if (productId != null && amount != null) {
        final product = _products.firstWhere(
          (p) => p.id == productId,
          orElse: () => Product(
            id: 0,
            name: '',
            price: 0.0,
            description: '',
            imageUrl: '',
            status: true,
            category: cat.Category(id: 0, name: '', status: 'A'),
          ),
        );
        total += product.price * amount;
      }
    }
    setState(() {
      _calculatedTotal = total;
    });
  }

  void _addProductDetailField() {
    setState(() {
      _productDetailControllers.add(_ProductDetailInputController());
    });
  }

  void _removeProductDetailField(int index) {
    setState(() {
      _productDetailControllers[index].dispose();
      _productDetailControllers.removeAt(index);
      _recalculateTotal();
    });
  }

  void _submitForm() {
    if (_formKey.currentState!.validate()) {
      // Crear el formato correcto que espera el backend
      final Map<String, dynamic> saleData = {
        'saleDate': DateTime.now().toIso8601String(),
        'totalPayment': _calculatedTotal,
        'delivery': _selectedDelivery,
        'deliveryAddress': _selectedDelivery == 'S' ? _deliveryAddressController.text : null,
        'note': _noteController.text.isNotEmpty ? _noteController.text : null,
        'userId': _selectedUserId,
        'id_type_state': _selectedIdTypeState,
        'id_payment_type': _selectedIdPaymentType,
        'productDetails': _productDetailControllers.map((controller) {
          return {
            'amount': int.parse(controller.amountController.text),
            'productId': int.parse(controller.productIdController.text)
          };
        }).toList(),
      };

      // Crear un SalesTicket con los datos en formato correcto
      final salesTicket = SalesTicket(
        ticketId: 0,
        saleDate: DateTime.now(),
        totalPayment: _calculatedTotal,
        delivery: _selectedDelivery,
        deliveryAddress: _selectedDelivery == 'S' ? _deliveryAddressController.text : null,
        note: _noteController.text.isNotEmpty ? _noteController.text : null,
        userId: _selectedUserId!,
        idTypeState: _selectedIdTypeState!,
        idPaymentType: _selectedIdPaymentType!,
        productDetails: _productDetailControllers.map((controller) {
          return ProductDetail(
            idDetailProduct: 0,
            ticketId: 0,
            amount: int.parse(controller.amountController.text),
            productId: int.parse(controller.productIdController.text),
          );
        }).toList(),
      );

      // Llamar al callback con los datos en formato correcto
      widget.onSubmit(salesTicket);
    }
  }

  Widget _buildProductDetailInputFields(int index) {
    final controller = _productDetailControllers[index];
    return Card(
      margin: const EdgeInsets.symmetric(vertical: 6.0),
      elevation: 2,
      child: Padding(
        padding: const EdgeInsets.all(12.0),
        child: Column(
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text('Producto  [${index + 1}'),
                IconButton(
                  icon: const Icon(Icons.remove_circle_outline),
                  onPressed: () => _removeProductDetailField(index),
                ),
              ],
            ),
            _loadingProducts
                ? const CircularProgressIndicator()
                : DropdownButtonFormField<int>(
                    decoration: const InputDecoration(labelText: 'Producto'),
                    value: controller.productIdController.text.isNotEmpty
                        ? int.tryParse(controller.productIdController.text)
                        : null,
                    items: _products
                        .map((product) => DropdownMenuItem<int>(
                              value: product.id,
                              child: Text(product.name),
                            ))
                        .toList(),
                    onChanged: (value) {
                      controller.productIdController.text =
                          value?.toString() ?? '';
                      _recalculateTotal();
                    },
                    validator: (value) =>
                        value == null ? 'Seleccione un producto' : null,
                  ),
            TextFormField(
              controller: controller.amountController,
              decoration: const InputDecoration(labelText: 'Cantidad'),
              keyboardType: TextInputType.number,
              onChanged: (_) => _recalculateTotal(),
              validator: (value) {
                if (value == null || value.isEmpty) return 'Ingrese cantidad';
                final amount = int.tryParse(value);
                if (amount == null || amount <= 0 || amount > 10) {
                  return 'Cantidad inválida (1-10)';
                }
                return null;
              },
            ),
          ],
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Text(widget.title),
      content: SingleChildScrollView(
        child: Form(
          key: _formKey,
          child: Column(
            children: [
              Row(
                children: [
                  const Text('Delivery:'),
                  Radio<String>(
                    value: 'S',
                    groupValue: _selectedDelivery,
                    onChanged: (v) => setState(() => _selectedDelivery = v!),
                  ),
                  const Text('Sí'),
                  Radio<String>(
                    value: 'N',
                    groupValue: _selectedDelivery,
                    onChanged: (v) => setState(() => _selectedDelivery = v!),
                  ),
                  const Text('No'),
                ],
              ),
              if (_selectedDelivery == 'S')
                TextFormField(
                  controller: _deliveryAddressController,
                  decoration: const InputDecoration(
                    labelText: 'Dirección de entrega',
                  ),
                  validator: (value) {
                    if (_selectedDelivery == 'S' &&
                        (value == null || value.isEmpty)) {
                      return 'Ingrese dirección de entrega';
                    }
                    return null;
                  },
                ),
              TextFormField(
                controller: _noteController,
                decoration: const InputDecoration(labelText: 'Nota (opcional)'),
                maxLines: 2,
              ),
              const SizedBox(height: 16),
              const Text(
                'Detalles de Productos:',
                style: TextStyle(fontWeight: FontWeight.bold),
              ),
              Column(
                children: List.generate(
                  _productDetailControllers.length,
                  (index) => _buildProductDetailInputFields(index),
                ),
              ),
              const SizedBox(height: 10),
              ElevatedButton(
                onPressed: _addProductDetailField,
                child: const Text('Agregar Producto'),
              ),
              const SizedBox(height: 16),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  const Text(
                    'Total a Pagar:',
                    style: TextStyle(fontWeight: FontWeight.bold),
                  ),
                  Text(
                    'S/  [${_calculatedTotal.toStringAsFixed(2)}',
                    style: const TextStyle(
                      fontSize: 16,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
      actions: [
        TextButton(
          onPressed: () => Navigator.of(context).pop(),
          child: const Text('Cancelar'),
        ),
        ElevatedButton(onPressed: _submitForm, child: const Text('Guardar')),
      ],
    );
  }
}

class _ProductDetailInputController {
  final TextEditingController productIdController = TextEditingController();
  final TextEditingController amountController = TextEditingController();

  void dispose() {
    productIdController.dispose();
    amountController.dispose();
  }
} 