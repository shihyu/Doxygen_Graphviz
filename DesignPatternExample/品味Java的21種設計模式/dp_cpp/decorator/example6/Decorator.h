#pragma once

#include "GoodsSaleEbi.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace decorator
			{
				namespace example6
				{
					///
					/// <summary> * װ�����Ľӿڣ���Ҫ����װ�εĶ���ʵ��ͬ���Ľӿ� </summary>
					/// 
					class Decorator : public GoodsSaleEbi
					{
					///	
					///	 <summary> * ���б�װ�ε�������� </summary>
					///	 
					protected:
						GoodsSaleEbi *ebi;
					///	
					///	 <summary> * ͨ�����췽�����뱻װ�εĶ��� </summary>
					///	 * <param name="ebi��װ�εĶ���"> </param>
					///	 
					public:
						Decorator(GoodsSaleEbi *ebi);
					};

				}
			}
		}
	}
}