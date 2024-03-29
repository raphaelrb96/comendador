package com.inc.comendador.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class CompraFinalParcelable implements Parcelable {

    String rua;
    double lat;
    double lng;
    String uidUserCompra;
    String userNome;
    String pathPhoto;
    int taxaEntrega;
    int somaProdutos;
    int itens;

    public CompraFinalParcelable(String rua, double lat, double lng, int itens, String uidUserCompra, String userNome, String pathPhoto, int taxaEntrega, int somaProdutos) {
        this.rua = rua;
        this.lat = lat;
        this.itens = itens;
        this.lng = lng;
        this.uidUserCompra = uidUserCompra;
        this.userNome = userNome;
        this.pathPhoto = pathPhoto;
        this.taxaEntrega = taxaEntrega;
        this.somaProdutos = somaProdutos;
    }


    protected CompraFinalParcelable(Parcel in) {
        rua = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
        uidUserCompra = in.readString();
        userNome = in.readString();
        pathPhoto = in.readString();
        taxaEntrega = in.readInt();
        somaProdutos = in.readInt();
        itens = in.readInt();
    }

    public static final Creator<CompraFinalParcelable> CREATOR = new Creator<CompraFinalParcelable>() {
        @Override
        public CompraFinalParcelable createFromParcel(Parcel in) {
            return new CompraFinalParcelable(in);
        }

        @Override
        public CompraFinalParcelable[] newArray(int size) {
            return new CompraFinalParcelable[size];
        }
    };

    public String getRua() {
        return rua;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public int getItens() {
        return itens;
    }

    public String getUidUserCompra() {
        return uidUserCompra;
    }

    public String getUserNome() {
        return userNome;
    }

    public String getPathPhoto() {
        return pathPhoto;
    }

    public int getTaxaEntrega() {
        return taxaEntrega;
    }

    public int getSomaProdutos() {
        return somaProdutos;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(rua);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeString(uidUserCompra);
        dest.writeString(userNome);
        dest.writeString(pathPhoto);
        dest.writeInt(taxaEntrega);
        dest.writeInt(somaProdutos);
        dest.writeInt(itens);
    }
}
