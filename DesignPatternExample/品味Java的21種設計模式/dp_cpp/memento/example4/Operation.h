#pragma once

#include "OperationApi.h"
#include "Memento.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace memento
			{
				namespace example4
				{
					///
					/// <summary> * �����࣬����ʵ�ּӼ������� </summary>
					/// 
					class Operation : public OperationApi
					{
					///	
					///	 <summary> * ��¼����Ľ�� </summary>
					///	 
					private:
						int result;
					public:
						virtual int getResult();

						virtual void add(int num);
						virtual void substract(int num);
						virtual Memento *createMemento();
						virtual void setMemento(Memento *memento);
					///	
					///	 <summary> * ����¼���� </summary>
					///	 
					private:
						class MementoImpl : public Memento
						{
						private:
							int result;
						public:
							MementoImpl(int result);

							virtual int getResult();
						};
					};

				}
			}
		}
	}
}