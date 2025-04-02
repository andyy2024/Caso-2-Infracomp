import matplotlib.pyplot as plt
import pandas as pd
import numpy as np
import matplotlib.ticker as ticker

# Leer archivo .txt
archivo = 'output/data.txt'
df = pd.read_csv(archivo, sep=r'\s+', header=0)

#--------------------------------------------------------------------------
# Gráfica 1: Número total de fallas de página (escala logarítmica)
#--------------------------------------------------------------------------
fig1, ax1 = plt.subplots(figsize=(10, 6))
cmap = plt.get_cmap('Pastel1')

# Posiciones de las barras en el eje x
posiciones_barras = np.arange(len(df['tamaño_pagina'].unique()))
ancho_barra = 0.1

# Agrupar por tamaño de página y marcos
for i, marcos in enumerate(df['marcos_asignados'].unique()):
    df_marcos = df[df['marcos_asignados'] == marcos]
    ax1.bar(posiciones_barras + i * ancho_barra, df_marcos['numero_fallas'], ancho_barra, label=f'{marcos} marcos', color=cmap(i))

ax1.set_xlabel('Tamaño de Página (Bytes)')
ax1.set_ylabel('Num Fallas (Nota: Escala logarítmica)')
ax1.set_title('Número Total de Fallas de Página\n(por Tamaño de Página y Marcos Asignados)')
ax1.set_xticks(posiciones_barras + ancho_barra * (len(df['marcos_asignados'].unique()) - 1) / 2)
ax1.set_xticklabels(df['tamaño_pagina'].unique())
ax1.legend()
# plt.legend(loc='center left', bbox_to_anchor=(1, 0.5)) 

ax1.set_yscale('log')
ax1.yaxis.set_major_formatter(ticker.ScalarFormatter())

max_falla = df["numero_fallas"].max()
print(max_falla)
log = np.log10(max_falla)
ceros = (int) (log//1)
escala_logaritmica = [10**x for x in range(ceros+1)]
ax1.set_yticks(escala_logaritmica)
print(escala_logaritmica)
ax1.grid(True)
fig1.tight_layout()
fig1.savefig("graphs/numero_fallas.png", dpi=300)

#--------------------------------------------------------------------------
# Gráfica 2: Número total de hits
#--------------------------------------------------------------------------
fig2, ax2 = plt.subplots(figsize=(10, 6))
# cmap = plt.get_cmap('RdPu')
# norm = plt.Normalize(0, 10)

cmap = plt.get_cmap('Pastel2')

for i, marcos in enumerate(df['marcos_asignados'].unique()):
    df_marcos = df[df['marcos_asignados'] == marcos]
    ax2.bar(posiciones_barras + i * ancho_barra, df_marcos['numero_hits'], ancho_barra, label=f'{marcos} marcos', color=cmap(i))

ax2.set_xlabel('Tamaño de Página (Bytes)')
ax2.set_ylabel('Num Hits')
ax2.set_title('Número Total de Hits\n(por Tamaño de Página y Marcos Asignados)')
ax2.set_xticks(posiciones_barras + ancho_barra * (len(df['marcos_asignados'].unique()) - 1) / 2)
ax2.set_xticklabels(df['tamaño_pagina'].unique())
ax2.set_ylim(df['numero_hits'].min() - 1000, df['numero_hits'].max() + 6000)
plt.legend(loc='center left', bbox_to_anchor=(1, 0.5)) 
# ax2.legend(loc='lower right')
ax2.grid()

fig2.tight_layout()
fig2.savefig("graphs/numero_hits.png", dpi=300)