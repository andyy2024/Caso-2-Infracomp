import pandas as pd

archivo = 'output/data.txt'
df = pd.read_csv(archivo, sep=r'\s+', header=0)
df.to_excel('Escritura_de_Datos.xlsx', index=False)